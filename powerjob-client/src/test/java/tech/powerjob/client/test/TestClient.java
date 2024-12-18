package tech.powerjob.client.test;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.powerjob.client.PowerJobClient;
import tech.powerjob.common.enums.ExecuteType;
import tech.powerjob.common.enums.ProcessorType;
import tech.powerjob.common.enums.TimeExpressionType;
import tech.powerjob.common.request.http.SaveJobInfoRequest;
import tech.powerjob.common.request.query.InstancePageQuery;
import tech.powerjob.common.response.InstanceInfoDTO;
import tech.powerjob.common.response.JobInfoDTO;
import tech.powerjob.common.response.ResultDTO;

/**
 * Test cases for {@link PowerJobClient}
 *
 * @author tjq
 * @author Echo009
 * @since 2020/4/15
 */
@Slf4j
class TestClient extends ClientInitializer {

    public static final long JOB_ID = 1L;

    @Test
    void testSaveJob() {

        SaveJobInfoRequest newJobInfo = new SaveJobInfoRequest();
        newJobInfo.setId(JOB_ID);
        newJobInfo.setJobName("omsOpenAPIJobccccc" + System.currentTimeMillis());
        newJobInfo.setJobDescription("test OpenAPI" + System.currentTimeMillis());
        newJobInfo.setJobParams("{'aa':'bb'}");
        newJobInfo.setTimeExpressionType(TimeExpressionType.CRON);
        newJobInfo.setTimeExpression("0 0 * * * ? ");
        newJobInfo.setExecuteType(ExecuteType.STANDALONE);
        newJobInfo.setProcessorType(ProcessorType.BUILT_IN);
        newJobInfo.setProcessorInfo("tech.powerjob.samples.processors.StandaloneProcessorDemo");
        newJobInfo.setDesignatedWorkers("");

        newJobInfo.setMinCpuCores(1.1);
        newJobInfo.setMinMemorySpace(1.2);
        newJobInfo.setMinDiskSpace(1.3);

        log.info("[TestClient] [testSaveJob] SaveJobInfoRequest: {}", JSONObject.toJSONString(newJobInfo));

        ResultDTO<Long> resultDTO = powerJobClient.saveJob(newJobInfo);
        log.info("[TestClient] [testSaveJob] result: {}", JSONObject.toJSONString(resultDTO));
        Assertions.assertNotNull(resultDTO);
    }

    @Test
    void testCopyJob() {
        ResultDTO<Long> copyJobRes = powerJobClient.copyJob(JOB_ID);
        System.out.println(JSONObject.toJSONString(copyJobRes));
        Assertions.assertNotNull(copyJobRes);
    }

    @Test
    void testExportJob() {
        ResultDTO<SaveJobInfoRequest> exportJobRes = powerJobClient.exportJob(JOB_ID);
        System.out.println(JSONObject.toJSONString(exportJobRes));
    }

    @Test
    void testFetchJob() {
        ResultDTO<JobInfoDTO> fetchJob = powerJobClient.fetchJob(JOB_ID);
        System.out.println(JSONObject.toJSONString(fetchJob));
        Assertions.assertNotNull(fetchJob);
    }

    @Test
    void testDisableJob() {
        ResultDTO<Void> res = powerJobClient.disableJob(JOB_ID);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }

    @Test
    void testEnableJob() {
        ResultDTO<Void> res = powerJobClient.enableJob(JOB_ID);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }

    @Test
    void testDeleteJob() {
        ResultDTO<Void> res = powerJobClient.deleteJob(JOB_ID);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }

    @Test
    void testRun() {
        ResultDTO<Long> res = powerJobClient.runJob(JOB_ID, null, 0);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }

    @Test
    void testRunJobDelay() {
        ResultDTO<Long> res = powerJobClient.runJob(JOB_ID, "this is instanceParams", 60000);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }

    @Test
    void testFetchInstanceInfo() {
        ResultDTO<InstanceInfoDTO> res = powerJobClient.fetchInstanceInfo(702482902331424832L);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }

    @Test
    void testQueryInstanceInfo() {
        InstancePageQuery instancePageQuery = new InstancePageQuery();
        instancePageQuery.setJobIdEq(11L);
        instancePageQuery.setSortBy("actualTriggerTime");
        instancePageQuery.setAsc(true);
        instancePageQuery.setPageSize(3);
        instancePageQuery.setStatusIn(Lists.newArrayList(1,2,5));
        TestUtils.output(powerJobClient.queryInstanceInfo(instancePageQuery));
    }

    @Test
    void testStopInstance() {
        ResultDTO<Void> res = powerJobClient.stopInstance(702482902331424832L);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }

    @Test
    void testFetchInstanceStatus() {
        ResultDTO<Integer> res = powerJobClient.fetchInstanceStatus(702482902331424832L);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }

    @Test
    void testCancelInstanceInTimeWheel() {
        ResultDTO<Long> startRes = powerJobClient.runJob(JOB_ID, "start by OhMyClient", 20000);
        System.out.println("runJob result: " + JSONObject.toJSONString(startRes));
        ResultDTO<Void> cancelRes = powerJobClient.cancelInstance(startRes.getData());
        System.out.println("cancelJob result: " + JSONObject.toJSONString(cancelRes));
        Assertions.assertTrue(cancelRes.isSuccess());
    }

//    @Test
//    @SneakyThrows
//    void testCancelInstanceInDatabase() {
//        ResultDTO<Long> startRes = powerJobClient.runJob(15L, "start by OhMyClient", 2000000);
//        System.out.println("runJob result: " + JSONObject.toJSONString(startRes));
//
//        // Restart server manually and clear all the data in time wheeler.
//        TimeUnit.MINUTES.sleep(1);
//
//        ResultDTO<Void> cancelRes = powerJobClient.cancelInstance(startRes.getData());
//        System.out.println("cancelJob result: " + JSONObject.toJSONString(cancelRes));
//        Assertions.assertTrue(cancelRes.isSuccess());
//    }

    @Test
    void testRetryInstance() {
        ResultDTO<Void> res = powerJobClient.retryInstance(169557545206153344L);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }
}
