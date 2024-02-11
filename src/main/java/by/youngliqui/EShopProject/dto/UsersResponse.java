package by.youngliqui.EShopProject.dto;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class UsersResponse {
    private List<UserDTO> users;

    public UsersResponse(List<UserDTO> users) {
        this.users = users;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
