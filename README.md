# changebook-elasticsearch
### Elastic Search

### pom.xml
```
<dependency>
  <groupId>io.github.changebooks</groupId>
  <artifactId>changebook-elasticsearch</artifactId>
  <version>1.0.1</version>
</dependency>
```

### application.yml
```
# Elastic Search
spring:
  elasticsearch:
    uris: 127.0.0.1:9200
```

### Application
```
@EnableElasticsearchRepositories(basePackages = {""})
```

### 请求和响应的日志
```
@Component
public class LogCustomizerImpl extends EsLogCustomizer {
}
```

### 例子

#### 实例
```
@Document(indexName = "user", createIndex = true)
public class User extends EsEntity<Long> {
    /**
     * 用户名
     */
    @Field(type = FieldType.Keyword)
    private String username;

    /**
     * 性别
     */
    @Field(type = FieldType.Integer)
    private Integer gender;

    /**
     * 年龄
     */
    @Field(type = FieldType.Integer)
    private Integer age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
```

#### 数仓
```
@Component
public interface UserRepository extends EsRepository<Long, User> {

    /**
     * 通过“性别”和“年龄”，查询列表
     *
     * @param gender   性别
     * @param age      年龄
     * @param pageable 分页参数
     * @return 用户列表
     */
    Page<User> findByGenderAndAgeOrderBySortAsc(Integer gender, Integer age, Pageable pageable);

}
```

#### 测试
```
@SpringBootTest
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = {"***"})
public class UserRepositoryTests {

    private static final IndexCoordinates INDEX_COORDINATES = IndexCoordinates.of("user");

    @Resource
    private UserRepository userRepository;

    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    public void testFindByGenderAndAgeOrderBySortAsc() {
        Pageable pageable = PageRequest.of(0, 2);

        Page<User> list = userRepository.findByGenderAndAgeOrderBySortAsc(1, 22, pageable);
        list.get().forEach(System.out::println);
    }

    @Test
    public void testFindByGenderAndAgeOrderBySortDesc() {
        Pageable pageable = PageRequest.of(0, 2);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.
                boolQuery().
                filter(QueryBuilders.termQuery("gender", 1)).
                filter(QueryBuilders.termQuery("age", 22));

        FieldSortBuilder fieldSortBuilder = SortBuilders.
                fieldSort("sort").
                order(SortOrder.DESC);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(boolQueryBuilder).
                withSorts(fieldSortBuilder).
                withPageable(pageable).
                build();

        SearchHits<User> searchHits = elasticsearchOperations.search(searchQuery, User.class, INDEX_COORDINATES);
        searchHits.get().forEach(System.out::println);
    }

    @Test
    public void testSave() {
        List<User> list = new ArrayList<>();

        for (int i = 1; i < 20; i++) {
            User user = new User();

            user.setId((long) i);
            user.setUsername("用户名-" + i);
            user.setGender(i % 2 + 1);
            user.setAge((new Random()).nextInt(3) + 20);
            user.setSort(i);
            user.setRemark("备注-" + i);
            user.setNote("内部备注-" + i);
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());

            list.add(user);
        }

        userRepository.saveAll(list);
    }

    @Test
    public void testDelete() {
        userRepository.deleteById(1L);
    }

}
```
