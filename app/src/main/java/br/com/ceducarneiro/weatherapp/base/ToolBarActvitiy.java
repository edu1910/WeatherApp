package br.com.ceducarneiro.weatherapp.base;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import br.com.ceducarneiro.weatherapp.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class ToolBarActvitiy extends ActionBarActivity {

    @Optional @InjectView(R.id.loading)
    protected View loading;

    @Optional @InjectView(R.id.toolbar)
    protected Toolbar toolbar;

    @Optional
    @InjectView(R.id.placeholder)
    protected View placeholder;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setLogo(R.drawable.ic_logo);
            getSupportActionBar().setTitle("");

            final ActionBar actionBar = getSupportActionBar();
            BitmapDrawable background = new BitmapDrawable(BitmapFactory.decodeResource(
                    getResources(), R.drawable.actionbar_background));
            background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
            actionBar.setBackgroundDrawable(background);
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    protected void showLoading(boolean visible) {
        if (loading != null) {
            loading.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

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

}
