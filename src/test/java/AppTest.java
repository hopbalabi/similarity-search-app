
/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import entities.Sku;
import stream.SkuGenerator;
import utils.Utils;

public class AppTest {

  private static String filePath = "/Users/ssimsek/projects/recommendation_service/home24-test-data.json";
  private static JSONObject jsonObjects;
  private static ExecutorService pool;
  private static AtomicBoolean inService;
  private static LinkedBlockingQueue<Sku> skuQueue;

  @BeforeClass
  public static void createJsonObjectFromFile() {
    jsonObjects = Utils.readJsonFile(filePath);
    pool = Executors.newFixedThreadPool(10);
    inService = new AtomicBoolean(true);
    skuQueue = new LinkedBlockingQueue<Sku>();
  }

  @Test
  public void testReadJSONFile() {
    Assert.assertNotNull(jsonObjects);
    Assert.assertTrue((jsonObjects instanceof JSONObject));
  }

  @Test
  public void testReadSku1() {
    JSONObject compareObject = jsonObjects.getJSONObject("sku-1");
    Assert.assertEquals(compareObject.get("att-a"), "att-a-7");
    Assert.assertNotEquals(compareObject.get("att-b"), "att-b-30");
    Assert.assertEquals(compareObject.get("att-c"), "att-c-10");
    Assert.assertEquals(compareObject.get("att-d"), "att-d-10");
    Assert.assertEquals(compareObject.get("att-e"), "att-e-15");
    Assert.assertNotEquals(compareObject.get("att-f"), "att-f-12");
    Assert.assertEquals(compareObject.get("att-g"), "att-g-2");
    Assert.assertEquals(compareObject.get("att-h"), "att-h-7");
    Assert.assertEquals(compareObject.get("att-i"), "att-i-5");
    Assert.assertNotEquals(compareObject.get("att-j"), "att-j-13");
  }

  @Test
  public void testSkuGeneratorQueue() {
    SkuGenerator skuGenerator = new SkuGenerator(jsonObjects, skuQueue);
    pool.submit(skuGenerator);
    waitForQueueSize(skuGenerator.getSkuQueue(), 20000);
    Assert.assertEquals(20000, skuGenerator.getSkuQueue().size());
  }

  @Test
  public void testSkuConsumer() {
    SkuGenerator consumerManager = new SkuGenerator(jsonObjects, skuQueue);
    pool.submit(consumerManager);
    waitAWhile(3000);
    Assert.assertEquals(20000, consumerManager.getSkuQueue().size());
  }

  private void waitForQueueSize(LinkedBlockingQueue<Sku> queue, int size) {
    while (queue.size() < size) {

    }
  }

  private void waitAWhile(int duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
