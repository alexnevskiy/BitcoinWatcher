import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
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
    public void getBitcoinPriceFromJSONTest() throws IOException {
        String fileString = getFileStringFromResources("/json/currentprice.json");

        float bitcoinPrice = app.getBitcoinPriceFromJSON(
                new JSONObject(fileString)
        );

        Assert.assertTrue(bitcoinPrice > 0);
    }

    @Test
    public void getBitcoinPriceTimeFromJSONTest() throws IOException {
        String fileString = getFileStringFromResources("/json/currentprice.json");

        String bitcoinPriceTime = app.getBitcoinPriceTimeFromJSON(
                new JSONObject(fileString)
        );

        Assert.assertEquals("2021-12-28 14:47:00", bitcoinPriceTime);
    }

    private String getFileStringFromResources(String path) throws IOException {
        InputStream inputStream = AppTest.class.getResourceAsStream(path);

        String fileString = "";

        if (inputStream != null) {
            fileString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }

        return fileString;
    }
}
