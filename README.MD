# Bitcoin Watcher

<img src="/images/Bitcoin_Watcher_logo.png" align="right" width="300" height="300" />

Bitcoin Watcher is a service that allows you to track the price of BTC in real time. You can just find out the price of bitcoin at the current moment, or set it for tracking. Upon reaching the set value, a notification about this will appear on your screen.

## Commands

- `get` - Get the current Bitcoin price
- `track number` - Track the rise in price or fall in price relative to a given `number`
- `list` - List all monitored prices
- `remove number` - Remove the tracked price value (`number`)
- `help` - Getting command description
- `exit` - Exit service

<p align="right"> master: <a href="https://github.com/alexnevskiy/BitcoinWatcher/actions?query=branch%3Amaster">
      <img src="https://github.com/alexnevskiy/BitcoinWatcher/actions/workflows/converter.yml/badge.svg?branch=master" align="right" ></a></p>
<p align="right"> develop: <a href="https://github.com/alexnevskiy/BitcoinWatcher/actions?query=branch%3Adevelop">
      <img src="https://github.com/alexnevskiy/BitcoinWatcher/actions/workflows/converter.yml/badge.svg?branch=develop" align="right" ></a></p>

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

![](/images/Notification_fallen.png)

Bitcoin price rise:

![](/images/Notification_raised.png)

Windows Notification List:

![](/images/Notification_list.png)
