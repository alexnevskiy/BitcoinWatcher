# Bitcoin Watcher

<img src="https://github.com/alexnevskiy/BitcoinWatcher/blob/develop-readme/images/Bitcoin_Watcher_logo.png?raw=true" align="right" style="zoom:20%; " />

Bitcoin Watcher is a service that allows you to track the price of BTC in real time. You can just find out the price of bitcoin at the current moment, or set it for tracking. Upon reaching the set value, a notification about this will appear on your screen.

## Commands

- `get` - Get the current Bitcoin price
- `track number` - Track the rise in price or fall in price relative to a given `number`
- `list` - List all monitored prices
- `remove number` - Remove the tracked price value (`number`)
- `help` - Getting command description
- `exit` - Exit service

## Usage

### Desktop

1. Clone the repository to your computer
2. Start service
3. Use the above commands
4. Enjoy the fall of bitcoin

### Docker

Unfortunately, system notifications are **not supported** when launched via docker :(

1. Clone the repository to your computer
2. Build an image using `docker build -t bitcoin-watcher .`
3. Run container using `docker run -it bitcoin-watcher`
4. Use the above commands
5. Enjoy the fall of bitcoin

## Work examples

### get

```
get
<2021-11-09 20:26:53> Current price = 67045.13$ on 2021-11-09 20:26:00 Etc/UTC time
```

### track

```
track 67150
Tracking started.
Tracking the growth of BTC up to 67150.0$
<2021-11-09 20:28:20> Current price = 67090.61$ on 2021-11-09 20:28:00 Etc/UTC time
track 65540.5
Tracking the fall of BTC to 65540.5$
<2021-11-09 20:29:20> Current price = 67064.984$ on 2021-11-09 20:29:00 Etc/UTC time
```

### list

```
list
Prices to track:
        UP      67150.0$
        DOWN    65540.5$
```

### remove

```
remove 67150
The price has been removed from the list.
list
Prices to track:
        DOWN    65540.5$
```

### help

```
help
exit - Exit service
get - Get the current Bitcoin price
track number - Track the rise in price or fall in price relative to a given value (number)
list - List all monitored prices
remove number - Remove the tracked price value (number)
help - Getting command description
```

### exit

```
exit
Goodbye! Hope you finally buy a video card :)
Tracking finished.
```

### Notification

Falling Bitcoin price:

![](https://github.com/alexnevskiy/BitcoinWatcher/blob/develop-readme/images/Notification_fallen.png?raw=true)

Bitcoin price rise:

![](https://github.com/alexnevskiy/BitcoinWatcher/blob/develop-readme/images/Notification_raised.png?raw=true)

Windows Notification List:

![](https://github.com/alexnevskiy/BitcoinWatcher/blob/develop-readme/images/Notification_list.png?raw=true)