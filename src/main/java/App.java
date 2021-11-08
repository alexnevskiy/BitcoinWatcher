import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {

    private static final String BITCOIN_CURRENT_PRICE = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private static final String EXIT = "exit";
    private static final String GET = "get";
    private static final String TRACK = "track";
    private static final String HELP = "help";
    private static final String GOODBYE = "Goodbye! Hope you finally buy a video card :)";
    private static final String ERROR = "Failed to get bitcoin price.";
    private static final String HELP_TEXT = "exit - Exit service\nget - Get the current Bitcoin price\n" +
            "track number - track the rise in price or fall in price relative to a given value (number)\n" +
            "help - Getting command description";
    private static final String BAD_COMMAND = "Bad command. Enter \"help\" for a description of the commands.";
    private static final String BAD_ENTRY = "Bad entry. Correct command: track number";
    private static final String BAD_PRICE = "Bad price.";
    private static final String TRACKING_FINISHED = "Tracking finished.";
    private static final long PERIOD = 60 * 1000;
    private final SimpleDateFormat simpleDateFormat;
    private final OkHttpClient client = new OkHttpClient();
    private Timer timer;
    private final List<Price> pricesToTrack;
    private float currentPrice;
    private String currentPriceTime;

    public static void main(String[] args) {
        App app = new App();
        app.scanConsole();
    }

    public App() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        simpleDateFormat = new SimpleDateFormat(pattern);
        pricesToTrack = new ArrayList<>();
    }

    private void getBitcoinPrice() throws IOException {
        Request request = new Request.Builder().url(BITCOIN_CURRENT_PRICE).build();
        String bitcoinJSON = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        parseBitcoinPrice(bitcoinJSON);
    }

    private void startTracking() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loadBitcoinPrice(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println(ERROR);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String bitcoinJSON = Objects.requireNonNull(response.body()).string();
                        parseBitcoinPrice(bitcoinJSON);
                        printBitcoinPrice();
                        checkTracking();
                    }
                });
            }
        }, 0, PERIOD);
    }

    private void stopTracking() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        System.out.println(TRACKING_FINISHED);
    }

    private void checkTracking() {
        Iterator<Price> iterator = pricesToTrack.iterator();
        while (iterator.hasNext()) {
            Price priceToTrack = iterator.next();
            if (priceToTrack.type.reached(currentPrice, priceToTrack.target)) {
                String message = priceToTrack.type.message(currentPrice, priceToTrack.target);
                System.out.println(message);
                iterator.remove();
            }
        }
        if (pricesToTrack.isEmpty()) {
            stopTracking();
        }
    }

    private void loadBitcoinPrice(Callback callback) {
        Request request = new Request.Builder().url(BITCOIN_CURRENT_PRICE).build();
        client.newCall(request).enqueue(callback);
    }

    private void parseBitcoinPrice(String bitcoinJSON) {
        JSONObject jsonObject = new JSONObject(bitcoinJSON);
        currentPrice = jsonObject.getJSONObject("bpi").getJSONObject("USD").getFloat("rate_float");
        currentPriceTime = getCurrentTime(jsonObject.getJSONObject("time").getString("updatedISO"));
    }

    private void printBitcoinPrice() {
        String time = simpleDateFormat.format(new Date());
        System.out.println("<" + time + ">" + " Current price = " + currentPrice + "$ on " + currentPriceTime + " " +
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

            switch (line.split("\\W+")[0]) {
                case EXIT:
                    command = EXIT;
                    System.out.println(GOODBYE);
                    break;
                case GET:
                    command = GET;
                    try {
                        getBitcoinPrice();
                        printBitcoinPrice();
                    } catch (IOException exception) {
                        System.out.println(ERROR);
                    }
                    break;
                case TRACK:
                    command = TRACK;
                    line = line.replaceAll("\\s+", " ");
                    String[] args = line.split("\\s");
                    if (args.length != 2) {
                        System.out.println(BAD_ENTRY);
                        break;
                    }

                    float price;
                    try {
                        price = Float.parseFloat(args[1]);
                    } catch (Exception ignored) {
                        System.out.println(BAD_PRICE);
                        break;
                    }
                    try {
                        getBitcoinPrice();
                    } catch (IOException exception) {
                        System.out.println(ERROR);
                    }
                    Price priceToTrack = new Price(currentPrice, price);
                    pricesToTrack.add(priceToTrack);
                    if (timer == null) {
                        startTracking();
                    }
                    switch (priceToTrack.type) {
                        case UP:
                            System.out.println("Tracking the growth of BTC up to " + price + "$");
                            break;
                        case DOWN:
                            System.out.println("Tracking the fall of BTC to " + price + "$");
                            break;
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
        stopTracking();
        System.exit(0);
    }
}
