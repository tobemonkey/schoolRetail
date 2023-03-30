let webSocket;
/**
 * websocket心跳检测
 */
var heartCheck = {
    timeout: 5000,
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function () {
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        return this;
    },
    start: function () {
        var self = this;
        this.timeoutObj && clearTimeout(this.timeoutObj);
        this.serverTimeoutObj && clearTimeout(this.serverTimeoutObj);
        this.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            webSocket.send("ping");
            self.serverTimeoutObj = setTimeout(function () { // 如果超过一定时间还没重置，说明后端主动断开了
                console.log('关闭服务');
                webSocket.close();//如果onclose会执行reconnect，我们执行 websocket.close()就行了.如果直接执行 reconnect 会触发onclose导致重连两次
            }, self.timeout)
        }, this.timeout)
    }
};

//避免重复连接
var lockReconnect = false, tt;
var reconnectCount = 10;

/**
 * websocket重连
 */
function reconnect(url) {
    if (lockReconnect) {
        return;
    }
    lockReconnect = true;
    tt && clearTimeout(tt);
    tt = setTimeout(function () {
        if (reconnectCount > 0) {
            console.log('重连中...剩余尝试次数：' + reconnectCount);
            lockReconnect = false;
            reconnectCount --;
            createWebSocket(url);
        } else {
            console.log("系统出现异常，暂时无法连接")
            cocoMessage.error("系统出现异常，暂时无法连接");
        }
    }, 4000);
}

/**
 * websocket启动
 */
function createWebSocket(url) {
    try {
        webSocket = new WebSocket(url);
        init(url);
    } catch (e) {
        console.log('catch' + e);
        reconnect(url);
    }

    function init(url) {
        //连接成功建立的回调方法
        webSocket.onopen = function (event) {
            reconnectCount = 10;
            console.log("WebSocket:已连接");
            //心跳检测重置
            heartCheck.reset().start();
        };

        //接收到消息的回调方法
        webSocket.onmessage = function (event) {
            console.log("WebSocket:收到一条消息", event.data);
            heartCheck.reset().start();
        };

        //连接发生错误的回调方法
        webSocket.onerror = function (event) {
            console.log("WebSocket:发生错误");
            reconnect(url);
        };

        //连接关闭的回调方法
        webSocket.onclose = function (event) {
            console.log("WebSocket:已关闭");
            if (event.code === 1001) {
                console.log("66666")
                cocoMessage.error(event.reason)
                return;
            }
            heartCheck.reset();//心跳检测
            reconnect(url);
        };

        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            webSocket.close(1001, "用户正常退出");
        };

    }
}