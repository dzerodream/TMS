function changeMonth(month) {
    // 获取当前URL
    const url = new URL(window.location.href);
    // 设置或更新month参数
    url.searchParams.set('month', month);
    // 使用新的URL进行跳转
    window.location.href = url.toString();
} 