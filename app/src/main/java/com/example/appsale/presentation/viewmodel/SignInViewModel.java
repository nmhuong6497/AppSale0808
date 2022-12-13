package com.example.appsale.presentation.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appsale.common.AppConstant;
import com.example.appsale.data.local.AppCache;
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

public class SignInViewModel extends ViewModel {
    private MutableLiveData<AppResource<User>> userResource = new MutableLiveData<>();
    private AuthenticationRepository repository;
    private AppCache appCache;

    public SignInViewModel(Context context) {
        repository = new AuthenticationRepository(context);
        appCache = AppCache.getInstance(context);
    }

    public LiveData<AppResource<User>> getUserResource() {
        return userResource;
    }

    public void signIn(String email, String password) {
        userResource.setValue(new AppResource.Loading<>(null));
        Call<AppResource<UserDTO>> callSignIn = repository.signIn(email, password);
        callSignIn.enqueue(new Callback<AppResource<UserDTO>>() {
            @Override
            public void onResponse(Call<AppResource<UserDTO>> call, Response<AppResource<UserDTO>> response) {
                if (response.isSuccessful()) {
                    AppResource<UserDTO> userDTOAppResource = response.body();
                    UserDTO userDTO = userDTOAppResource.data;
                    User user = new User(userDTO.getEmail(), userDTO.getName(), userDTO.getPhone(), userDTO.getToken());
                    userResource.setValue(new AppResource.Success<>(user));
                    appCache.saveDataString(AppConstant.KEY_VALUE, user.getToken());
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
