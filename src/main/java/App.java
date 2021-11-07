import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class App {

    private static final String BITCOIN_CURRENT_PRICE = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private static final String EXIT = "exit";
    private static final String GET = "get";
    private static final String HELP = "help";
    private static final String GOODBYE = "Goodbye! Hope you finally buy a video card :)";
    private static final String ERROR = "Failed to get bitcoin price.";
    private static final String HELP_TEXT = "exit - Exit service\nget - Get the current Bitcoin price\n" +
            "help - Getting command description";
    private static final String BAD_COMMAND = "Bad command. Enter \"help\" for a description of the commands.";
    private final SimpleDateFormat simpleDateFormat;
    private final OkHttpClient client = new OkHttpClient();
    private float currentPrice;

    public static void main(String[] args) {
        App app = new App();
        app.scanConsole();
    }

    public App() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        simpleDateFormat = new SimpleDateFormat(pattern);
    }

    private void getBitcoinPrice() throws IOException {
        Request request = new Request.Builder().url(BITCOIN_CURRENT_PRICE).build();
        String bitcoinJSON = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        parseBitcoinPrice(bitcoinJSON);
    }

    private void parseBitcoinPrice(String bitcoinJSON) {
        JSONObject jsonObject = new JSONObject(bitcoinJSON);
        currentPrice = jsonObject.getJSONObject("bpi").getJSONObject("USD").getFloat("rate_float");
        String timeJSON = getCurrentTime(jsonObject.getJSONObject("time").getString("updatedISO"));
        String time = simpleDateFormat.format(new Date());
        System.out.println("<" + time + ">" + " Current price = " + currentPrice + "$ on " + timeJSON + " " +
                ZoneId.systemDefault().getId() + " time");
    }

    private String getCurrentTime(String time) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz"));
        ZonedDateTime currentZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        return currentZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void scanConsole() {
        Scanner scanner = new Scanner(System.in);
        String command = null;

        while (!EXIT.equals(command)) {
            String line = scanner.nextLine().toLowerCase(Locale.ROOT).trim();

            switch (line) {
                case EXIT:
                    command = EXIT;
                    System.out.println(GOODBYE);
                    break;
                case GET:
                    command = GET;
                    try {
                        getBitcoinPrice();
                    } catch (IOException exception) {
                        System.out.println(ERROR);
                    }
                    break;
                case HELP:
                    command = HELP;
                    System.out.println(HELP_TEXT);
                    break;
                default:
                    System.out.println(BAD_COMMAND);
                    break;
            }
        }
        scanner.close();
        System.exit(0);
    }
}
