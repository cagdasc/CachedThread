package com.cacaosd.cachedthread.model.output;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cagdascaglak on 5.02.2018.
 */

public class BaseRestOutput implements Parcelable {
    private int responseCode;
    private AppStatus appStatus;
    private String accessToken;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public AppStatus getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(AppStatus appStatus) {
        this.appStatus = appStatus;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public enum AppStatus {

        DEMO(0), TEST(1), RELEASE(2);

        private int status;

        AppStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public BaseRestOutput() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.responseCode);
        dest.writeInt(this.appStatus == null ? -1 : this.appStatus.ordinal());
        dest.writeString(this.accessToken);
    }

    protected BaseRestOutput(Parcel in) {
        this.responseCode = in.readInt();
        int tmpAppStatus = in.readInt();
        this.appStatus = tmpAppStatus == -1 ? null : AppStatus.values()[tmpAppStatus];
        this.accessToken = in.readString();
    }

    public static final Creator<BaseRestOutput> CREATOR = new Creator<BaseRestOutput>() {
        @Override
        public BaseRestOutput createFromParcel(Parcel source) {
            return new BaseRestOutput(source);
        }

        @Override
        public BaseRestOutput[] newArray(int size) {
            return new BaseRestOutput[size];
        }
    };
}
