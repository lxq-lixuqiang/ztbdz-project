
// 数据库连接管理脚本
function connectDB() {
    fetch('/connect-db', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            password: '123456'
        })
    })
    .then(response => response.json())
    .then(data => {
        if(data.success) {
            alert('数据库连接成功');
            location.reload();
        } else {
            alert('连接失败: ' + data.message);
        }
    })
    .catch(error => {
        alert('连接错误: ' + error);
    });
}

function disconnectDB() {
    fetch('/disconnect-db', {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if(data.success) {
            alert('数据库已断开');
            location.reload();
        } else {
            alert('断开失败: ' + data.message);
        }
    })
    .catch(error => {
        alert('断开错误: ' + error);
    });
}
