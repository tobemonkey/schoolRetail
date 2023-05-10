window.onload = function () {
    createWebSocket("ws://localhost:8080/websocket/");
}


// todo 心跳检测功能，因为存在不知名 bug，暂时跳过
// 显示箭头
// 动态生成轮播图圆圈
// 点击圆圈图片移动
// 箭头点击事件
// 自动播放功能
let time = 2;
let flag = false;
let timer = setInterval(function() {
    arrowRight.click();
}, 2000);

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


let ul = banner.querySelector('ul');
let ol = banner.querySelector('ol');
let ulChildren = ul.children;
let offset = banner.offsetWidth;
for(let i = 0; i < ulChildren.length;i ++) {
    let li = document.createElement('li');
    li.setAttribute('index', i);
    li.addEventListener('mouseenter', function() {
        for(let j = 0;j < ol.children.length;j ++) {
            ol.children[j].className = '';
        }
        this.className = 'current';
        let index = this.getAttribute('index');
        animate(ul, -index*offset);
    });
    ol.appendChild(li);

}
ol.children[0].className = 'current';
const num = ol.children.length;
arrowLeft.addEventListener('click', function() {
    let index = Number(document.querySelector('.current').getAttribute('index'));
    index = (index + num - 1) % num;
    animate(ul, -index*offset);
    for(let j = 0;j < ol.children.length;j ++) {
        ol.children[j].className = '';
    }
    ol.children[index].className = 'current';
})
arrowRight.addEventListener('click', function() {
    let index = Number(document.querySelector('.current').getAttribute('index'));
    index = (index + 1) % num;
    animate(ul, -index*offset);
    for(let j = 0;j < ol.children.length;j ++) {
        ol.children[j].className = '';
    }
    ol.children[index].className = 'current';
})



// 节流阀
let throttleFlag = false;

// 目录显示
let catalogue = document.querySelector('.catalogue');
window.addEventListener('load', function() {
    if(window.innerWidth < 1700) {
        catalogue.style.display = 'none';
    } else {
        catalogue.style.display = 'block'
    }
})
window.addEventListener('resize', function() {
    if(window.innerWidth < 1700) {
        catalogue.style.display = 'none';
    } else {
        catalogue.style.display = 'block'
    }
})


