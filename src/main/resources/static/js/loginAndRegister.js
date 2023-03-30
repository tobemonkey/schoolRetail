const container = document.querySelector('#container');
const signInButton = document.querySelector('#signIn');
const signUpButton = document.querySelector('#signUp');

// 注册页面动态效果
signUpButton.addEventListener('click', () => container.classList.add('right-panel-active'))
signInButton.addEventListener('click', () => container.classList.remove('right-panel-active'))

// 验证码功能
const sendBtn = document.getElementById("send_code");
const email = document.getElementById("email");
sendBtn.addEventListener("click", function () {
    if (checkEmail(email)) {
        cocoMessage.warn("请先填写您的邮箱！");
        return;
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
        url: 'http://localhost:8080/loginAndRegister/verify?email=' + email.value,
        type: 'get',
        success: data => {
            showInfo(data);
        }
    });
})

// 参数校验
let password = document.getElementById("password");
let verifyCode = document.getElementById("verify_code");

function checkEmail(e) {
    let pattern = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    return pattern.test(e.value);
}

function checkPassword(pwdValue) {
    if (pwdValue.length === 0)
        return true;
    let pattern = new RegExp("^[a-zA-Z0-9]{10,16}$");
    return pattern.test(pwdValue);
}

email.addEventListener("blur", () => {
    if (!checkEmail(email)) {
        email.style.borderColor = "red";
        email.style.color = "red";
        cocoMessage.error("邮箱格式错误！请检查");
    }
});
email.addEventListener("focus", () => {
    email.style.borderColor = "#ccc";
    email.style.color = "black";
})

password.addEventListener("blur", () => {
    if (!checkPassword(password.value)) {
        password.style.borderColor = "red";
        password.style.color = "red";
        cocoMessage.error("密码长度必须为长度10-16的字母数字字符串！");
    }
});
password.addEventListener("focus", () => {
    password.style.borderColor = "#ccc";
    password.style.color = "black";
})

// 注册功能
const register = document.getElementById("register");
register.addEventListener("click", () => {

    register.disabled = true;

    const passwordValue = password.value;
    const emailValue = email.value;
    const verifyCodeValue = verifyCode.value;

    if (verifyCodeValue.length === 0) {
        cocoMessage.error("请填写验证码！");
        return;
    }

    if (checkEmail(email) && checkPassword(passwordValue) && emailValue.length > 0 && passwordValue.length > 0) {
        let jsonData = {'password': passwordValue, 'email': emailValue, 'verifyCode': verifyCodeValue};
        $.ajax({
            dataType: 'json',
            contentType: 'application/json;charset=utf-8',
            url: "http://localhost:8080/loginAndRegister/register",
            data: JSON.stringify(jsonData),
            type: "POST",
            success: function(data) {
                register.disabled = false;
                const retData = data['data'];
                const stateCode = data['status'];
                if (stateCode < 0) {
                    cocoMessage.error(retData);
                } else if (stateCode > 0) {
                    cocoMessage.info(retData);
                    signInButton.click();
                }
            }
        })
    } else {
        cocoMessage.error("请检查信息是否填写完整！");
    }

})

// 登录功能
let rememberCheck = document.getElementById("remember");
const loginBtn = document.getElementById("login");
// flag 为 true 表示手动点击，不适用token进行登入
let flag = false;
window.onload = function () {
    const token = window.localStorage.getItem("token");
    const emailInfo = window.localStorage.getItem("email");
    const loginEmail = document.getElementById("login-email");
    const loginPassword = document.getElementById("login-password");
    if (token != null && emailInfo != null) {
        loginEmail.value = emailInfo;
        loginPassword.value = "12345678912";
        rememberCheck.checked = true;
        flag = true;
        loginBtn.click();
    }
}

// 如果选中记住密码，则获取 token 并记住
loginBtn.addEventListener("click", () => {

    // 如果保存了 token
    const token = window.localStorage.getItem("token");

    const loginEmail = document.getElementById("login-email");
    const loginPassword = document.getElementById("login-password");

    if (!checkEmail(loginEmail)) {
        cocoMessage.error("邮箱格式错误！");
        return;
    }
    if (!checkPassword(loginPassword.value)) {
        cocoMessage.error("密码长度必须为长度10-16的字母数字字符串！");
        return;
    }

    // 拼接数据
    let dataJson = {'password': loginPassword.value, 'email': loginEmail.value, 'verifyCode': 'NULL'};
    $.ajax({
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        url: "https://www.baidu.com",
        type: "POST",
        data: JSON.stringify(dataJson),
        beforeSend: request => {
            request.withCredentials = true;
            if (token != null && flag)
                request.setRequestHeader("token", token);
        },
        success: data => {
            loginPassword.value = '';
            flag = false;
            const status = data['status'];
            const token = data['data'];
            if (status.startsWith('0')) {
                // 存储 token
                window.localStorage.setItem("token", token);
                window.localStorage.setItem("email", loginEmail.value);

                // window.location.href = "http://localhost:8080/";
                $.http()
            } else {
                cocoMessage.error(data['data'])
            }
        }
    })
})

function showInfo(data) {
    var status = data['status'];
    var response = data['data'];
    if (status.startsWith('-1')) {
        cocoMessage.error(response);
    } else if (status.startsWith('-2')) {
        cocoMessage.warn(response);
    } else if (status.startsWith('0')) {
        cocoMessage.info(response);
    } else {
        cocoMessage.error(response);
    }
}
