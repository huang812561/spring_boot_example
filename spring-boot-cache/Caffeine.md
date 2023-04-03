# Java8本地缓存Caffeine
## 一、Caffeine缓存
### 1.缓存介绍

    Caffeine是基于JDK1.8版本的高性能本地缓存库，
    它是Guava的增强版，与ConcurrentLinkedHashMap相似，
    支持并发，并且可以在O(1)的时间复杂度内查找、写入元素。
 互联网通常基于两种缓存:

- 本地缓存
- 分布式缓存
### 二.Caffeine使用
#### 2.1 缓存加载策略
`参见示例:CaffeineUtils`
- 2.1.1 Cache 手动创建
- 2.1.2 Loading Cache 自动创建
- 2.1.3 Async Cache异步获取
#### 2.2 驱逐策略
    驱逐策略一般在创建缓存时指定，一般基于时间和容量
- 2.2.1 LRU最近最少使用，淘汰最长时间没有使用的
- 2.2.2 LFU最近不常使用，一段时间内使用次数最少的
- 2.2.3 FIFO 先进先出

### 2.3 Caffeine缓存过期策略
- expireAfterAccess: 当缓存项在指定的时间段内没有被读或写就会被回收。
- expireAfterWrite：当缓存项在指定的时间段内没有更新就会被回收（移除key），需要等待获取新值才会返回。
- refreshAfterWrite：创建缓存或者最近一次更新缓存后经过指定时间间隔，触发刷新缓存，并立刻返回旧值
    注意：refreshAfterWrite 仅支持 LoadingCache、AsyncLoadingCache
### 2.4 总结
    
    策略在创建时都可以进行自由组合，一般情况下有两种方法

- 设置 maxSize、refreshAfterWrite，不设置 expireAfterWrite/expireAfterAccess，
    设置expireAfterWrite当缓存过期时会同步加锁获取缓存，所以设置expireAfterWrite时性能较好，但是某些时候会取旧数据,适合允许取到旧数据的场景
- 设置 maxSize、expireAfterWrite/expireAfterAccess，不设置 refreshAfterWrite;
    数据一致性好，不会获取到旧数据，但是性能没那么好（对比起来），适合获取数据时不耗时的场景

## 三、SpringBoot整合Caffeine
### 1、@Cacheable相关注解
#### 1.1 相关依赖
如果要使用@Cacheable注解，需要引入相关依赖，并在任一配置类文件上添加@EnableCaching注解

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>

#### 1.2 常用注解
- @Cacheable：表示该方法支持缓存。当调用被注解的方法时，如果对应的键已经存在缓存，则不再执行方法体，而从缓存中直接返回。当方法返回null时，将不进行缓存操作。
- @CachePut：表示执行该方法后，其值将作为最新结果更新到缓存中，每次都会执行该方法。
- @CacheEvict：表示执行该方法后，将触发缓存清除操作。
- @Caching：用于组合前三个注解，例如：

        @Caching(cacheable = @Cacheable("CacheConstants.GET_USER"),
        evict = {@CacheEvict("CacheConstants.GET_DYNAMIC",allEntries = true)}
        public User find(Integer id) {
            return null;
        }

#### 1.3 常用注解属性
- cacheNames/value：缓存组件的名字，即cacheManager中缓存的名称。
- key：缓存数据时使用的key。默认使用方法参数值，也可以使用SpEL表达式进行编写。
- keyGenerator：和key二选一使用。
- cacheManager：指定使用的缓存管理器。
- condition：在方法执行开始前检查，在符合condition的情况下，进行缓存
- unless：在方法执行完成后检查，在符合unless的情况下，不进行缓存
- sync：是否使用同步模式。若使用同步模式，在多个线程同时对一个key进行load时，其他线程将被阻塞。

#### 1.4 缓存同步模式

    sync开启或关闭，在Cache和LoadingCache中的表现是不一致的：

- Cache中，sync表示是否需要所有线程同步等待
- LoadingCache中，sync表示在读取不存在/已驱逐的key时，是否执行被注解方法

### 2、实战
#### 2.1 引入pom依赖
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        
        <dependency>
            <groupId>com.github.ben-manes.caffeine</groupId>
            <artifactId>caffeine</artifactId>
        </dependency>

#### 2.2 自定义缓存常量 CacheConstants

    创建缓存常量类，把公共的常量提取一层，复用，这里也可以通过配置文件加载这些数据，例如@ConfigurationProperties和@Value
    
    public class CacheConstants {
    /**
    * 默认过期时间（配置类中我使用的时间单位是秒，所以这里如 3*60 为3分钟）
    */
    public static final int DEFAULT_EXPIRES = 3 * 60;
    public static final int EXPIRES_5_MIN = 5 * 60;
    public static final int EXPIRES_10_MIN = 10 * 60;
    
        public static final String GET_USER = "GET:USER";
        public static final String GET_DYNAMIC = "GET:DYNAMIC";
    
    }

#### 2.3 缓存配置类CacheConfig

        @Configuration
        @EnableCaching
        public class CacheConfig {
            /**
            * Caffeine配置说明：
            * initialCapacity=[integer]: 初始的缓存空间大小
            * maximumSize=[long]: 缓存的最大条数
            * maximumWeight=[long]: 缓存的最大权重
            * expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期
            * expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期
            * refreshAfterWrite=[duration]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
            * weakKeys: 打开key的弱引用
            * weakValues：打开value的弱引用
            * softValues：打开value的软引用
            * recordStats：开发统计功能
            * 注意：
            * expireAfterWrite和expireAfterAccess同事存在时，以expireAfterWrite为准。
            * maximumSize和maximumWeight不可以同时使用
            * weakValues和softValues不可以同时使用
            */
            @Bean
            public CacheManager cacheManager() {
                SimpleCacheManager cacheManager = new SimpleCacheManager();
                List<CaffeineCache> list = new ArrayList<>();
                //循环添加枚举类中自定义的缓存，可以自定义
                for (CacheEnum cacheEnum : CacheEnum.values()) {
                    list.add(new CaffeineCache(cacheEnum.getName(),
                    Caffeine.newBuilder()
                        .initialCapacity(50)
                        .maximumSize(1000)
                        .expireAfterAccess(cacheEnum.getExpires(), TimeUnit.SECONDS)
                        .build()));
                }
                cacheManager.setCaches(list);
                return cacheManager;
            }
        }
    
#### 2.4 缓存调用

    这里要注意的是Cache和@Transactional一样也使用了代理，类内调用将失效
    /**
        * value：缓存key的前缀。
        * key：缓存key的后缀。
        * sync：设置如果缓存过期是不是只放一个请求去请求数据库，其他请求阻塞，默认是false（根据个人需求）。
        * unless：不缓存空值,这里不使用，会报错
        * 查询用户信息类
        * 如果需要加自定义字符串，需要用单引号
        * 如果查询为null，也会被缓存
      */
      @Cacheable(value = CacheConstants.GET_USER,key = "'user'+#userId",sync = true)
      @CacheEvict
      public UserEntity getUserByUserId(Integer userId){
          UserEntity userEntity = userMapper.findById(userId);
          System.out.println("查询了数据库");
          return userEntity;
      }

