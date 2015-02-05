CREATE TABLE user (
  userId INT IDENTITY (1, 1) NOT NULL
    CONSTRAINT pk_user_userId PRIMARY KEY,

  name   VARCHAR(20)         NOT NULL,

  CONSTRAINT uq_user_name UNIQUE (name)
);

CREATE TABLE project (
  projectId INT IDENTITY (1, 1) NOT NULL
    CONSTRAINT pk_project_projectId PRIMARY KEY,

  name      VARCHAR(20)         NOT NULL,

  CONSTRAINT uq_project_name UNIQUE (name)
);

CREATE TABLE projectUser (
  projectUserId INT IDENTITY (1, 1) NOT NULL
    CONSTRAINT pk_projectUser_projectUserId PRIMARY KEY,

  projectId     INT                 NOT NULL,
  userId        INT                 NOT NULL,
  datetime      DATETIME            NOT NULL,

  CONSTRAINT fk_projectUser_projectId FOREIGN KEY (projectId)
  REFERENCES project (projectId),

  CONSTRAINT fk_projectUser_userId FOREIGN KEY (userId)
  REFERENCES user (userId),

  CONSTRAINT uq_projectUser_projectId_userId UNIQUE (projectId, userId)
);