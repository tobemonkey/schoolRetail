// 验证码功能
const sendBtn = document.getElementById("get_code");
sendBtn.addEventListener("click", function () {
    let emailValue = document.getElementById("email").value;
    if (emailValue.length === 0) {
        cocoMessage.error("请先填写您的邮箱");
        return ;
    } else {
        let pattern = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        if (!pattern.test(emailValue)) {
            return;
        }
    }
    cocoMessage.info("请求发送成功！请稍候");
    sendBtn.style.backgroundColor = "#626569";
    sendBtn.disabled = true;
    sendBtn.style.cursor = "auto";
    let time = 60;
    // 开启定时器
    let timer = setInterval(function () {
        // 判断剩余秒数
        if (time === 0) {
            // 清除定时器和复原按钮
            clearInterval(timer);
            sendBtn.disabled = false;
            sendBtn.style.backgroundColor = "#417dff";
            sendBtn.style.cursor = "pointer";
            sendBtn.innerHTML = '获取验证码';
        } else {
            sendBtn.innerHTML = `${time}秒后重新获取`;
            time--;
        }
    }, 1000);
    $.ajax({
        url: 'http://localhost:8080/login/oauth2/github/verify/' + emailValue,
        type: 'get',
        success: data => {
            cocoMessage.info(data['data']);
        }
    });
})

// 提交功能
let submitBtn = document.getElementById("submit");
// 1. 参数校验
let email = document.getElementById("email");
let verifyCode = document.getElementById("verify");
function checkEmail() {
    let pattern = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    return pattern.test(email.value);
}

email.addEventListener("blur", () => {
    if (!checkEmail()) {
        email.style.borderColor = "red";
        email.style.color = "red";
        cocoMessage.error("邮箱格式错误！请检查");
    }
});

email.addEventListener("focus", () => {
    email.style.borderColor = "#ccc";
    email.style.color = "black";
})

// 2. 提交
submitBtn.addEventListener('click', () => {
    if (!checkEmail()) {
        email.style.borderColor = "red";
        email.style.color = "red";
        cocoMessage.error("邮箱格式错误！请检查");
        return ;
    }
    if (verifyCode.value.length !== 6) {
        verifyCode.style.borderColor = "red";
        verifyCode.style.color = "red";
        cocoMessage.error("验证码应该为6位！请检查");
        return;
    }

    submitBtn.disable = true;
    var href = window.location.href;
    let jsonData = {'email': email.value, 'verifyCode': verifyCode.value};

    $.ajax({
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        url: 'http://localhost:8080/login/oauth2/github/commit/' +  href.slice(href.lastIndexOf('/')+1),
        type: 'post',            
        data: JSON.stringify(jsonData),
        success: function(data) {
            submitBtn.disable = false;
            if (data['status'] !== '0001') {
                cocoMessage.error(data['data']['msg'])
                if (data['data']['status'] === '3002') {
                    cocoMessage.error("身份超时，即将跳转重新绑定");
                    setInterval(() => {
                        window.location.href = "http://localhost:8080/loginAndRegister";
                    }, 3000)
                }
            } else {
                cocoMessage.info(data['data']['msg'])
                setInterval(() => {
                    window.location.href = "http://localhost:8080/";
                }, 3000)
            }
        }
    });
})