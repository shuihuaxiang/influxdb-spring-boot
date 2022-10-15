# influxdb-spring-boot
一个操作influxDB的starter
## 依赖
    
            <dependency>
                <groupId>com.kim</groupId>
                <artifactId>influxdb-spring-boot-starter</artifactId>
                <version>1.0.1</version>
            </dependency>
            
## 使用

> 实体类

    @Data
    @Measurement(name = "quota")
    public class QuotaInfo {
        @Column(name = "host",tag = true)
        private String host;
    
        @Column(name = "region",tag = true)
        private String region;
    
        @Column(name = "quotaId",tag = true)
        private String quotaId;
    
        @Column(name = "quotaName",tag = true)
        private String quotaName;
    
        @Column(name = "unit",tag = true)
        private String unit;
    
        @Column(name = "value")
        private Double value;
    
        @Column(name="time")
        private String time;
    }

## 测试类 

    @SpringBootTest
    class InfluxdbStarterTest {
    
        @Autowired
        private InfluxTemplate influxTemplate;
    
    
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
            quotaInfo2.setHost("server01");
            quotaInfo2.setRegion("zh-east");
            quotaInfo2.setQuotaId("00003");
            quotaInfo2.setQuotaName("温度");
            quotaInfo2.setUnit("摄氏度");
            quotaInfo2.setValue(12.40D);
            quotaInfoList.add(quotaInfo2);
            influxTemplate.insert(quotaInfoList);
        }
    
        @Test
        public void ListTest() {
            QueryAllBuilder queryAllBuilder = new QueryAllBuilder(QuotaInfo.class);
            Map<String, String> map = new HashMap<>();
    //        map.put("quotaId", "00003");
    //        map.put("host", "server01");
            queryAllBuilder.where(map)
    //                .start("2022-10-14 03:29:17.66")
    //                .end("2022-10-14 11:05:17.44")
    //                .page(1L, 5L)
                    .groupBy("host", "quotaId")
                    .orderBy(SortOrders.ASC)
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
    
        @Test
        public void deleteTest() {
            DeleteBuilder deleteBuilder = new DeleteBuilder("quota");
            Map<String, String> map = new HashMap<>();
            map.put("host", "server03");
            deleteBuilder.where(map).build();
            influxTemplate.delete(deleteBuilder);
        }
    
        @Test
        public void executeTest() {
            String sql = "select * from quota";
            influxTemplate.execute(sql);
        }
    
    }


