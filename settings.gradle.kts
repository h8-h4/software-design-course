rootProject.name = "software-design"
include("lru-cache")
include("api")
include("refactoring")
include("mvc")
include("clock")
include("expression")
include("bridge")
include("actor")
include("event-sourcing")

include("testcontainers:exchange")
findProject(":testcontainers:exchange")?.name = "exchange"