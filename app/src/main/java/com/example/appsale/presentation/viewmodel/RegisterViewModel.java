package com.example.appsale.presentation.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appsale.data.model.AppResource;
import com.example.appsale.data.model.User;
import com.example.appsale.data.remote.dto.UserDTO;
import com.example.appsale.data.repository.AuthenticationRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<AppResource<User>> userResource = new MutableLiveData<>();
    private AuthenticationRepository repository;

    public RegisterViewModel(Context context) {
        repository = new AuthenticationRepository(context);
    }

    public LiveData<AppResource<User>> getUserResource() {
        return userResource;
    }

    public void register(String email, String password, String name, String phone, String address) {
        userResource.setValue(new AppResource.Loading<>(null));
        Call<AppResource<UserDTO>> callSignIn = repository.register(email, password, name, phone, address);
        callSignIn.enqueue(new Callback<AppResource<UserDTO>>() {
            @Override
            public void onResponse(Call<AppResource<UserDTO>> call, Response<AppResource<UserDTO>> response) {
                if (response.isSuccessful()) {
                    AppResource<UserDTO> userDTOAppResource = response.body();
                    UserDTO userDTO = userDTOAppResource.data;
                    User user = new User(userDTO.getEmail(), userDTO.getName(), userDTO.getPhone(), userDTO.getToken());
                    userResource.setValue(new AppResource.Success<>(user));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        userResource.setValue(new AppResource.Error<>(message));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AppResource<UserDTO>> call, Throwable t) {
                userResource.setValue(new AppResource.Error<>(t.getMessage()));
            }
        });
    }
}
