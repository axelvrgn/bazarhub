        package com.example.bazarhub.Models;

        public class UserResponse {

            private String email;
            private String password;
            private String name;
            private String avatar;
            private String role;
            private int id;

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role.toLowerCase();
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
