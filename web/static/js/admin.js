// 添加菜单项的活动状态
document.addEventListener('DOMContentLoaded', function () {
    const menuItems = document.querySelectorAll('.menu-items a');
    const currentPath = window.location.pathname;

    menuItems.forEach(item => {
        if (item.getAttribute('href') === currentPath) {
            item.parentElement.classList.add('active');
        }
    });
}); 