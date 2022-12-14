### Примеры использования:

```shell
> ./gradlew :bridge:run --args="-awt -list" -q --console=plain
1 0 2
2 0 1
0 1 2                                                                                                     
```

![img](images/img.png)

```shell
> ./gradlew :bridge:run --args="-awt -matrix" -q --console=plain
0 1 1 1 1
1 0 1 1 1
1 1 0 1 1
1 1 1 0 1
1 1 1 1 0
                                                                                                 
```

![img](images/img1.png)

```shell
> ./gradlew :bridge:run --args="-awt -list" -q --console=plain
0 1
1 2
2 3
3 4
4 5
5 0
                                                                                       
```

![img](images/img2.png)

```shell
> ./gradlew :bridge:run --args="-ascii -list" -q --console=plain
1 0 2
2 0 1
0 1 2
                                                                                                                                                                                                                                                                                              
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                 *****                                                              
                                *     *                                                             
                               *       *                                                            
                              *         *                                                           
                              *         *                                                           
                              *    ●    *                                                           
                              *    ●●●  *                                                           
                              *    ●  ●●*                                                           
                               *   ●   *●●                                                          
                                *  ●  *   ●                                                         
                                 **●**     ●●                                                       
                                   ●         ●●                                                     
                                   ●           ●                                                    
                                   ●            ●●                                                  
                                   ●              ●●                                                
                                   ●                ●●                                              
                                   ●                  ●                                             
                                   ●                   ●●                                           
                                   ●                     ●●                                         
                                   ●                       ●●                                       
                                   ●                         ●                                      
                                   ●                          ●●                                    
                                   ●                            ●●                                  
                                   ●                              ●●                                
                                   ●                                ●                               
                                   ●                                 ●●                             
                                   ●                                   ●●     *****                 
                                   ●                                     ●   *     *                
                                   ●                                      ●●*       *               
                                   ●                                       *●●       *              
                                   ●                                       *  ●●     *              
                                   ●                                       *    ●    *              
                                   ●                                       *  ●●     *              
                                   ●                                       *●●       *              
                                   ●                                      ●●*       *               
                                   ●                                     ●   *     *                
                                   ●                                   ●●     *****                 
                                   ●                                 ●●                             
                                   ●                                ●                               
                                   ●                              ●●                                
                                   ●                            ●●                                  
                                   ●                          ●●                                    
                                   ●                         ●                                      
                                   ●                       ●●                                       
                                   ●                     ●●                                         
                                   ●                   ●●                                           
                                   ●                  ●                                             
                                   ●                ●●                                              
                                   ●              ●●                                                
                                   ●            ●●                                                  
                                   ●           ●                                                    
                                   ●         ●●                                                     
                                 **●**     ●●                                                       
                                *  ●  *   ●                                                         
                               *   ●   *●●                                                          
                              *    ●  ●●*                                                           
                              *    ●●●  *                                                           
                              *    ●    *                                                           
                              *         *                                                           
                              *         *                                                           
                               *       *                                                            
                                *     *                                                             
                                 *****                                                              
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                              
```

```shell
> ./gradlew :bridge:run --args="-ascii -list" -q --console=plain
0 1
1 2
2 3
3 4
4 5
5 0
                                                                                                                                                                                          
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                 *****                         *****                                
                                *     *                       *     *                               
                               *       *                     *       *                              
                              *         *                   *         *                             
                              *         *                   *         *                             
                              *    ●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●    *                             
                              *   ●     *                   *     ●   *                             
                              *   ●     *                   *     ●   *                             
                               * ●     *                     *     ● *                              
                                *●    *                       *    ●*                               
                                ●*****                         *****●                               
                                ●                                   ●                               
                               ●                                     ●                              
                              ●                                       ●                             
                              ●                                       ●                             
                             ●                                         ●                            
                             ●                                         ●                            
                            ●                                           ●                           
                            ●                                            ●                          
                           ●                                             ●                          
                          ●                                               ●                         
                          ●                                               ●                         
                         ●                                                 ●                        
                         ●                                                 ●                        
                        ●                                                   ●                       
                       ●                                                     ●                      
                  *****●                                                     ●*****                 
                 *    ●*                                                     *●    *                
                *     ● *                                                   * ●     *               
               *     ●   *                                                 *   ●     *              
               *     ●   *                                                 *   ●     *              
               *    ●    *                                                 *    ●    *              
               *     ●   *                                                 *   ●     *              
               *     ●   *                                                 *   ●     *              
                *     ● *                                                   * ●     *               
                 *    ●*                                                     *●    *                
                  *****●                                                     ●*****                 
                       ●                                                     ●                      
                        ●                                                   ●                       
                         ●                                                 ●                        
                         ●                                                 ●                        
                          ●                                               ●                         
                          ●                                               ●                         
                           ●                                             ●                          
                           ●                                            ●                           
                            ●                                           ●                           
                             ●                                         ●                            
                             ●                                         ●                            
                              ●                                       ●                             
                              ●                                       ●                             
                               ●                                     ●                              
                                ●                                   ●                               
                                ●*****                         *****●                               
                                *●    *                       *    ●*                               
                               * ●     *                     *     ● *                              
                              *   ●     *                   *     ●   *                             
                              *   ●     *                   *     ●   *                             
                              *    ●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●    *                             
                              *         *                   *         *                             
                              *         *                   *         *                             
                               *       *                     *       *                              
                                *     *                       *     *                               
                                 *****                         *****                                
                                                                                                    
                                                                                      
```
