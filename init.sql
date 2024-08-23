CREATE USER [lab1funcProject1] FROM EXTERNAL PROVIDER;
ALTER ROLE db_datareader ADD MEMBER [lab1funcProject1];
ALTER ROLE db_datawriter ADD MEMBER [lab1funcProject1];

CREATE TABLE [UploadLog] (
    [Id] [int] IDENTITY PRIMARY KEY,
    [BlobName] [nvarchar](128) NOT NULL,
    [Size] [int] NOT NULL,
    [CreateTime] [date] NOT NULL DEFAULT (GETDATE())
)
