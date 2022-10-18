# influxdb-spring-boot
**一个操作influxDB的starter，为了日常方便使用**  

支持**注解**与**template**两种方式。

**会持续更新，欢迎关注**

## 依赖 
> 当前版本1.0.6
    
            <dependency>
                <groupId>com.kim</groupId>
                <artifactId>influxdb-spring-boot-starter</artifactId>
                <version>1.0.1</version>
            </dependency>
   
            
## 使用示例

    @SpringBootTest
    class InfluxdbStarterTest {
    
        @Autowired
        private InfluxTemplate influxTemplate;
    
        @Resource
        private TestInfluxMapper testInfluxMapper;
        //influxTemplate新增
        @Test
        public void addObjectTest() {
            List<Object> quotaInfoList = new ArrayList<>();
            QuotaInfo quotaInfo = new QuotaInfo();
            quotaInfo.setHost("server03");
            quotaInfo.setRegion("zh-east");
            quotaInfo.setQuotaId("00001");
            quotaInfo.setQuotaName("温度");
            quotaInfo.setUnit("摄氏度");
            quotaInfo.setValue(12.10D);
            quotaInfoList.add(quotaInfo);
            QuotaInfo quotaInfo2 = new QuotaInfo();
            quotaInfo2.setHost("server03");
            quotaInfo2.setRegion("zh-east");
            quotaInfo2.setQuotaId("00002");
            quotaInfo2.setQuotaName("温度");
            quotaInfo2.setUnit("摄氏度");
            quotaInfo2.setValue(12.40D);
            quotaInfoList.add(quotaInfo2);
            influxTemplate.insert(quotaInfoList);
        }
        //influxTemplate查询
        @Test
        public void ListTest() {
            QueryAllBuilder queryAllBuilder = new QueryAllBuilder(QuotaInfo.class);
            Map<String, String> map = new HashMap<>();
            map.put("quotaId", "00003");
            map.put("host", "server01");
            queryAllBuilder.where(map) //查询条件
                    .start("2022-10-14 03:29:17.66")
                    .end("2022-10-14 11:05:17.44")
                    .page(1L, 5L)//分页
                    .groupBy("host", "quotaId") //分组
                    .orderBy(SortOrders.ASC) //排序
                    .build();
            List<QuotaInfo> resultList = influxTemplate.select(queryAllBuilder);
            for (QuotaInfo quotaInfo : resultList) {
                //2020-09-19T09:58:34.926Z   DateTimeFormatter.ISO_OFFSET_DATE_TIME
                //转换为 2020-09-19 09:58:34  格式
                LocalDateTime dateTime = LocalDateTime.parse(quotaInfo.getTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                String time = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                quotaInfo.setTime(time);
            }
        }
        //influxTemplate 删除
        @Test
        public void deleteTest() {
            DeleteBuilder deleteBuilder = new DeleteBuilder("quota");
            Map<String, String> map = new HashMap<>();
            map.put("host", "server01");
            deleteBuilder.where(map).build();
            influxTemplate.delete(deleteBuilder);
        }
        //influxTemplate手写sql执行
        @Test
        public void executeTest() {
            String sql = "select * from quota";
            influxTemplate.execute(sql);
        }
        //Insert注解插入
        @Test
        public void influxMapperInsertTest() {
            QuotaInfo quotaInfo = new QuotaInfo();
            quotaInfo.setHost("server03");
            quotaInfo.setRegion("zh-east");
            quotaInfo.setQuotaId("00001");
            quotaInfo.setQuotaName("温度");
            quotaInfo.setUnit("摄氏度");
            quotaInfo.setValue(5.00D);
            int i = testInfluxMapper.insert(quotaInfo);
            System.out.println("======"+i);
        }
        //Insert注解批量插入
        @Test
        public void influxMapperInsertListTest() {
            List<QuotaInfo> quotaInfoList = new ArrayList<>();
            QuotaInfo quotaInfo = new QuotaInfo();
            quotaInfo.setHost("server03");
            quotaInfo.setRegion("zh-east");
            quotaInfo.setQuotaId("00001");
            quotaInfo.setQuotaName("温度");
            quotaInfo.setUnit("摄氏度");
            quotaInfo.setValue(4.00D);
            quotaInfoList.add(quotaInfo);
            QuotaInfo quotaInfo2 = new QuotaInfo();
            quotaInfo2.setHost("server03");
            quotaInfo2.setRegion("zh-east");
            quotaInfo2.setQuotaId("00002");
            quotaInfo2.setQuotaName("温度");
            quotaInfo2.setUnit("摄氏度");
            quotaInfo2.setValue(3.00D);
            quotaInfoList.add(quotaInfo2);
            int i = testInfluxMapper.insertBatch(quotaInfoList);
            System.out.println("======"+i);
        }
        //注解select查询
         @Test
        public void influxMapperListTest(){
             List<QuotaInfo> list = testInfluxMapper.getlist("server03", "00001","2022-10-18 21:44:17.44");
             System.err.println(list.toString());
         }
    
        //注解delete
        @Test
        public void influxMapperDeleteTest(){
            testInfluxMapper.delete("server03");
        }
    
    }
    
    
