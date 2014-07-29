    package com.br.models;

    import java.io.Serializable;

    /**
     * Created by Berhell on 28/07/14.
     */
    public class User implements Serializable {

        private String objectId;
        private String nickname;
        private String email;
        private String gender;
        private String profileAvatar;

        public User() {

        }

        public void setObjectId(String o) {
            this.objectId = o;
        }

        public String getObjectId() {
            return this.objectId;
        }

        public void setNickname(String n) {
            this.nickname = n;
        }

        public String getNickname() {
            return this.nickname;
        }

        public void setEmail(String e){
            this.email = e;
        }

        public String getEmail(){
            return this.email;
        }

        public void setGender(String g){
            this.gender = g;
        }

        public String getGender(){
            return this.gender;
        }

        public void setProfileAvatar(String p){
            this.profileAvatar = p;
        }

        public String getProfileAvatar(){
            return this.profileAvatar;
        }

    }
