window.onload = function () {
    // 如果有 token，则尝试建立长连接
    let token = window.localStorage.getItem("token");
    if (token != null) {
        createWebSocket("ws://localhost:8080/loginAndRegister/login/" + token);
    }
}


// 显示箭头
let banner = document.querySelector('.banner');
let arrowLeft = document.querySelector('.arrow-left');
let arrowRight = document.querySelector('.arrow-right');

banner.addEventListener('mouseenter', function() {
    arrowLeft.style.display = 'block';
    arrowRight.style.display = 'block';
    clearInterval(timer);
    timer = null;
});

banner.addEventListener('mouseleave', function() {
    arrowLeft.style.display = 'none';
    arrowRight.style.display = 'none';
    timer = setInterval(function() {
        arrowRight.click();
    }, 2000);
});


// 动态生成轮播图圆圈
let ul = banner.querySelector('ul');
let ol = banner.querySelector('ol');
let ulChildren = ul.children;
var offset = banner.offsetWidth;
for(var i = 0; i < ulChildren.length;i ++) {
    var li = document.createElement('li');
    li.setAttribute('index', i);
    li.addEventListener('mouseenter', function() {
        for(var j = 0;j < ol.children.length;j ++) {
            ol.children[j].className = '';
        }
        this.className = 'current';
        // 点击圆圈图片移动
        var index = this.getAttribute('index');
        animate(ul, -index*offset);
    });
    ol.appendChild(li);
    
}
ol.children[0].className = 'current';
const num = ol.children.length;
// 箭头点击事件
arrowLeft.addEventListener('click', function() {
    var index = Number(document.querySelector('.current').getAttribute('index'));
    index = (index + num - 1) % num;
    animate(ul, -index*offset);
    for(var j = 0;j < ol.children.length;j ++) {
        ol.children[j].className = '';
    }
    ol.children[index].className = 'current';
})
arrowRight.addEventListener('click', function() {
    var index = Number(document.querySelector('.current').getAttribute('index'));
    index = (index + 1) % num;
    animate(ul, -index*offset);
    for(var j = 0;j < ol.children.length;j ++) {
        ol.children[j].className = '';
    }
    ol.children[index].className = 'current';
})

// 自动播放功能
var time = 2;
var flag = false;
var timer = setInterval(function() {
    arrowRight.click();
}, 2000);


// 节流阀
var throttleFlag = false;

// 目录显示
let catalogue = document.querySelector('.catalogue');
window.addEventListener('load', function() {
    if(window.innerWidth < 1700) {
        console.log(123);
        catalogue.style.display = 'none';
    } else {
        catalogue.style.display = 'block'
    }
})
window.addEventListener('resize', function() {
    if(window.innerWidth < 1700) {
        console.log(123);
        catalogue.style.display = 'none';
    } else {
        catalogue.style.display = 'block'
    }
})


