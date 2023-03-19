package actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.japi.pf.ReceiveBuilder;
import model.SearchService;
import model.message.SearchReply;
import model.message.SearchRequest;
import scala.concurrent.duration.Duration;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MasterActor extends AbstractActor {
    private final MasterConfig masterConfig;
    private final Consumer<SearchReply> callback;
    private final AtomicInteger replyCount;

    private SearchReply reply = new SearchReply(Collections.emptyList());

    public MasterActor(MasterConfig masterConfig, Consumer<SearchReply> callback) {
        this.masterConfig = masterConfig;
        this.callback = callback;
        this.replyCount = new AtomicInteger(masterConfig.getServices().size());

        for (SearchService service : masterConfig.getServices()) {
            context().actorOf(
                    Props.create(ChildActor.class, service), service.getName()
            );
        }
    }

    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(ReceiveTimeout.class, (t) -> sendReply())
                .match(SearchRequest.class, this::requestChildren)
                .match(SearchReply.class, this::mergeReplies)
                .build();
    }

    private void sendReply() {
        callback.accept(reply);

        context().children().foreach(it -> {
            context().stop(it);
            return null;
        });
        context().stop(self());
    }

    private void requestChildren(SearchRequest request) {
        context().setReceiveTimeout(Duration.fromNanos(masterConfig.getTimeout().toNanos()));
        context().children().foreach(it -> {
            it.tell(request, self());
            return null;
        });
    }

    private void mergeReplies(SearchReply newReply) {
        reply = reply.withLinks(
                Stream.concat(reply.getLinks().stream(), newReply.getLinks().stream()).toList()
        );

        if (replyCount.decrementAndGet() == 0) {
            sendReply();
        }
    }
}
