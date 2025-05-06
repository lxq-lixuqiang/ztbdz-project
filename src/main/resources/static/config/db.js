
// 数据库配置文件
const mysql = require('mysql');

const dbConfig = {
    host: 'localhost',
    user: 'root', 
    password: '123456',
    database: 'bid_system'
};

// 创建连接池
const pool = mysql.createPool(dbConfig);

// 封装查询方法
function query(sql, params) {
    return new Promise((resolve, reject) => {
        pool.getConnection((err, connection) => {
            if (err) {
                reject(err);
                return;
            }
            
            connection.query(sql, params, (error, results) => {
                connection.release();
                if (error) {
                    reject(error);
                } else {
                    resolve(results);
                }
            });
        });
    });
}

module.exports = {
    query
};
