
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

// 提交评审报告数据
function submitReviewForm(formData) {
    return fetch('/api/review/submit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .catch(error => {
        console.error('提交错误:', error);
        throw error;
    });
}

// 获取项目列表
function getProjectList() {
    return fetch('/api/projects', {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => response.json())
    .catch(error => {
        console.error('获取项目列表错误:', error);
        throw error;
    });
}

// 初始化表单提交事件
function initFormSubmit(formId, callback) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        const formData = new FormData(form);
        const jsonData = {};
        formData.forEach((value, key) => {
            jsonData[key] = value;
        });

        callback(jsonData)
            .then(data => {
                if(data.success) {
                    alert('提交成功');
                    form.reset();
                } else {
                    alert('提交失败: ' + data.message);
                }
            })
            .catch(error => {
                alert('提交错误: ' + error.message);
            });
    });
}

// 用户注册功能
function registerUser(userData) {
    return fetch('http://localhost:8080/user/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: userData.username,
            password: userData.password,
            phone: userData.phone,
            email: userData.email
        })
    })
    .then(response => response.json())
    .catch(error => {
        console.error('注册错误:', error);
        throw error;
    });
}

// 增强的MySQL连接配置
const dbConfig = {
    host: 'localhost',
    port: 3306,
    user: 'ztbdz_user',
    password: 'secure_password', // 实际使用时应从安全配置读取
    database: 'ztbdz',
    connectionLimit: 10,
    waitForConnections: true,
    queueLimit: 0
};

// 测试数据库连接
function testConnection() {
    return fetch('/api/db/test-connection', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        body: JSON.stringify(dbConfig)
    })
    .then(response => response.json())
    .catch(error => {
        console.error('连接测试错误:', error);
        throw error;
    });
}

// 页面加载时初始化
document.addEventListener('DOMContentLoaded', function() {
    // 初始化注册表单
    if (document.getElementById('registerForm')) {
        initFormSubmit('registerForm', registerUser);
    }

    // 初始化评审表单
    if (document.getElementById('reviewForm')) {
        initFormSubmit('reviewForm', submitReviewForm);
    }

    // 加载项目列表
    if (document.getElementById('projectSelect')) {
        getProjectList().then(data => {
            if (data.success) {
                const select = document.getElementById('projectSelect');
                data.projects.forEach(project => {
                    const option = document.createElement('option');
                    option.value = project.id;
                    option.textContent = project.name;
                    select.appendChild(option);
                });
            }
        });
    }

    // 测试数据库连接
    if (document.getElementById('testConnectionBtn')) {
        document.getElementById('testConnectionBtn').addEventListener('click', testConnection);
    }
});