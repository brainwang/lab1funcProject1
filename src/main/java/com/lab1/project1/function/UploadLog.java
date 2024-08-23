package com.lab1.project1.function;

/**
 * 项目： lab1funcProject1
 * <p>
 *
 * @author : Bo Wang
 * @date : 8/23/2024
 */
import java.util.Date;

public class UploadLog {
  private Integer Id;// INT IDENTITY PRIMARY KEY,
  private String BlobName;// NVARCHAR(128) NOT NULL,
  private Integer Size;// BIGINT NOT NULL,
  private Date CreateTime;// DATE NOT NULL DEFAULT (GETDATE())
  public UploadLog(String blobName, int size) {
    this.Id = null;
    this.BlobName = blobName;
    this.Size = size;
    this.CreateTime = new Date();
  }

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    this.Id = id;
  }

  public String getBlobName() {
    return BlobName;
  }

  public void setBlobName(String blobName) {
    this.BlobName = blobName;
  }

  public Integer getSize() {
    return Size;
  }

  public void setSize(Integer size) {
    this.Size = size;
  }

  public Date getCreateTime() {
    return CreateTime;
  }

  public void setCreateTime(Date createTime) {
    this.CreateTime = createTime;
  }

  @Override
  public String toString() {
    return "UploadLog{" +
            "Id=" + Id +
            ", BlobName='" + BlobName + '\'' +
            ", Size=" + Size +
            ", CreateTime=" + CreateTime +
            '}';
  }
}

