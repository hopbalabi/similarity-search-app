import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

import config.Config;
import entities.Sku;
import stream.SkuConsumerManager;
import stream.SkuGenerator;
import utils.Utils;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
  private SkuGenerator skuGenerator;
  private SkuConsumerManager skuConsumerManager;
  private ExecutorService pool;
  private AtomicBoolean inService;
  private static LinkedBlockingQueue<Sku> skuQueue;
  private PriorityBlockingQueue<Sku> orderedQueue;
  private JSONObject skuObjects;
  private static String filePath = "/Users/ssimsek/projects/recommendation_service/home24-test-data.json";

  public App() {
    Config.configureLogger();
    init();
  }

  private void init() {
    pool = Executors.newCachedThreadPool();
    inService = new AtomicBoolean(true);
    skuQueue = new LinkedBlockingQueue<Sku>();
    orderedQueue = new PriorityBlockingQueue<Sku>(2048,
        (s1, s2) -> Integer.compare(s1.getSimilarity(), s2.getSimilarity()));
    skuObjects = Utils.readJsonFile(filePath);
    // Create sku stream by reading the json file.
    skuGenerator = new SkuGenerator(skuObjects, skuQueue, inService);

    // Create sku consumer manager for consuming the skus.
    skuConsumerManager = new SkuConsumerManager(skuQueue, orderedQueue, inService, pool,
        Sku.Map.toSku("sku-1", skuObjects.getJSONObject("sku-1")), skuObjects.keySet().size());

    pool.submit(skuGenerator);
    pool.submit(skuConsumerManager);
  }

  public static void main(String[] args) {
    new App();
  }
}
