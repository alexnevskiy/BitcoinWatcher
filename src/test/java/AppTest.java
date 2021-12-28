import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AppTest {
    App app = new App();

    @Test
    public void getBitcoinPriceTest() throws IOException {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
        Response response = app.getBitcoinPrice(url);
        Assert.assertNotNull(response.body());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void getBitcoinPriceTestInvalidUrl() throws IOException {
        String invalidUrl = "https://api.coindesk.com/v1/bpi/currentprice.jsonInvalid";
        Response response = app.getBitcoinPrice(invalidUrl);
        Assert.assertNotNull(response.body());
        Assert.assertEquals(404, response.code());
    }

    @Test
    public void getBitcoinPriceFromJSONTest() throws IOException, URISyntaxException {
        File file = getFileFromResources(File.separator + "json" + File.separator + "currentprice.json");

        float bitcoinPrice = app.getBitcoinPriceFromJSON(
                new JSONObject(FileUtils.readFileToString(file, StandardCharsets.UTF_8))
        );

        Assert.assertTrue(bitcoinPrice > 0);
    }

    @Test
    public void getBitcoinPriceTimeFromJSONTest() throws IOException, URISyntaxException {
        File file = getFileFromResources(File.separator + "json" + File.separator + "currentprice.json");

        String bitcoinPriceTime = app.getBitcoinPriceTimeFromJSON(
                new JSONObject(FileUtils.readFileToString(file, StandardCharsets.UTF_8))
        );

        Assert.assertEquals("2021-12-28 14:47:00", bitcoinPriceTime);
    }

    private boolean hasFileJSON(URL url) {
        return url != null && url.getFile() != null;
    }

    private File getFileFromResources(String path) throws URISyntaxException {
        URL url = AppTest.class.getClassLoader().getResource(path);
        URI fileURI = new URI("");

        if (hasFileJSON(url)) {
            fileURI = url.toURI();
        }

        return new File(fileURI);
    }
}
