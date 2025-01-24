// 院系管理员页面的通用JavaScript函数
document.addEventListener('DOMContentLoaded', function () {
    // 高亮当前菜单项
    highlightCurrentMenuItem();
});

function highlightCurrentMenuItem() {
    const currentPath = window.location.pathname;
    const menuItems = document.querySelectorAll('.menu-items a');

    menuItems.forEach(item => {
        if (currentPath.includes(item.getAttribute('href'))) {
            item.parentElement.classList.add('active');
        }
    });
} 