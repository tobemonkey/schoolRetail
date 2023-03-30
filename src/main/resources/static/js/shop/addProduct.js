window.onload = function() {
    // 通过返回的数据修饰下拉框
    session
    // 将 token 放入form表单中
    $('#token').val(window.localStorage.getItem('token'));
}