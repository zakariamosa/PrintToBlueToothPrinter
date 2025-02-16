// Generated by view binder compiler. Do not edit!
package com.example.printtobluetouthprinter.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.printtobluetouthprinter.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHomeBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView connectionStatus;

  @NonNull
  public final RecyclerView deviceRecyclerView;

  @NonNull
  public final Button printButton;

  @NonNull
  public final TextView printStatus;

  @NonNull
  public final EditText textInput;

  private FragmentHomeBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView connectionStatus, @NonNull RecyclerView deviceRecyclerView,
      @NonNull Button printButton, @NonNull TextView printStatus, @NonNull EditText textInput) {
    this.rootView = rootView;
    this.connectionStatus = connectionStatus;
    this.deviceRecyclerView = deviceRecyclerView;
    this.printButton = printButton;
    this.printStatus = printStatus;
    this.textInput = textInput;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.connectionStatus;
      TextView connectionStatus = ViewBindings.findChildViewById(rootView, id);
      if (connectionStatus == null) {
        break missingId;
      }

      id = R.id.deviceRecyclerView;
      RecyclerView deviceRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (deviceRecyclerView == null) {
        break missingId;
      }

      id = R.id.printButton;
      Button printButton = ViewBindings.findChildViewById(rootView, id);
      if (printButton == null) {
        break missingId;
      }

      id = R.id.printStatus;
      TextView printStatus = ViewBindings.findChildViewById(rootView, id);
      if (printStatus == null) {
        break missingId;
      }

      id = R.id.textInput;
      EditText textInput = ViewBindings.findChildViewById(rootView, id);
      if (textInput == null) {
        break missingId;
      }

      return new FragmentHomeBinding((ConstraintLayout) rootView, connectionStatus,
          deviceRecyclerView, printButton, printStatus, textInput);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
