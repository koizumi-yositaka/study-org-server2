-- T_MEETING テーブルの削除
DROP TABLE IF EXISTS T_MEETING;

-- T_USER テーブルの削除
DROP TABLE IF EXISTS T_USER;

-- T_AUTHORITY テーブルの削除
DROP TABLE IF EXISTS T_AUTHORITY;

CREATE TABLE T_AUTHORITY (
    id CHAR(1) PRIMARY KEY,             -- 権限ID (数値型)
    rank VARCHAR(20) NOT NULL UNIQUE,   -- 権限ランク (例: 'A', 'B', 'C')
    description VARCHAR(255)        -- 権限の説明 (例: 'Admin Level', 'User Level')
);
CREATE TABLE T_USER (
    id INT AUTO_INCREMENT PRIMARY KEY, -- ユーザーID (自動インクリメント)
    email VARCHAR(255) NOT NULL UNIQUE, -- メールアドレス
    password VARCHAR(255) NOT NULL,     -- パスワード（ハッシュ化）
    authority_id CHAR(1) NOT NULL,          -- 権限ID（外部キー）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 作成日時
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新日時
    FOREIGN KEY (authority_id) REFERENCES T_AUTHORITY(id) -- 外部キー制約
);
CREATE TABLE T_MEETING (
    id INT AUTO_INCREMENT PRIMARY KEY, -- ユーザーID (自動インクリメント)
    title VARCHAR(255) NOT NULL, -- メールアドレス
    detail VARCHAR(500) NOT NULL,     -- パスワード（ハッシュ化）
    opener_id INT NOT NULL,          -- 権限ID（外部キー）
    event_date DATETIME, -- 開催日時
    start_time CHAR(4) NOT NULL,
    end_time CHAR(4) NOT NULL,
    delete_flg CHAR(1) DEFAULT '0' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 作成日時
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新日時
    FOREIGN KEY (opener_id) REFERENCES T_USER(id) -- 外部キー制約
);