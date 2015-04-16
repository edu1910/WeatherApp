package br.com.ceducarneiro.weatherapp.base;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import br.com.ceducarneiro.weatherapp.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class ToolBarFragment extends Fragment {

    @Optional
    @InjectView(R.id.placeholder)
    protected View placeholder;

    protected void setPlaceholderVisible(boolean visible) {
        if (placeholder != null) {
            placeholder.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    protected void setPlaceholderMessage(String message) {
        if (placeholder != null) {
            TextView txMessage = ButterKnife.findById(placeholder, R.id.placeholder_message);
            txMessage.setText(message);
        }
    }

    protected void setPlaceholderMessage(int messageResId) {
        setPlaceholderMessage(getString(messageResId));
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

}
