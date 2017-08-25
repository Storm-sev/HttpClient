package com.cheng315.chengnfc.bean;

/**
 * Created by Administrator on 2017/8/25.
 */

public class VersionBean {


    /**
     * code : 0
     * data : {"id":"1","platform":"2","versionId":"1","subVersionId":"1.1","versionCode":"V1_1.1","apkUrl":"http://m.315cheng.com/resource/315cheng.apk","status":"1"}
     */

    private String code;
    private DataBean data;

    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * id : 1
         * platform : 2
         * versionId : 1
         * subVersionId : 1.1
         * versionCode : V1_1.1
         * apkUrl : http://m.315cheng.com/resource/315cheng.apk
         * status : 1
         */

        private String id;
        private String platform;
        private String versionId;
        private String subVersionId;
        private String versionCode;
        private String apkUrl;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String versionId) {
            this.versionId = versionId;
        }

        public String getSubVersionId() {
            return subVersionId;
        }

        public void setSubVersionId(String subVersionId) {
            this.subVersionId = subVersionId;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getApkUrl() {
            return apkUrl;
        }

        public void setApkUrl(String apkUrl) {
            this.apkUrl = apkUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}


