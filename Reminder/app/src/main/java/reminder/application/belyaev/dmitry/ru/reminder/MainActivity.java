package reminder.application.belyaev.dmitry.ru.reminder;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import reminder.application.belyaev.dmitry.ru.reminder.adapter.TabAdapter;
import reminder.application.belyaev.dmitry.ru.reminder.dialog.AddingTaskDialogFragment;
import reminder.application.belyaev.dmitry.ru.reminder.fragment.SplashFragment;

public class MainActivity extends AppCompatActivity implements AddingTaskDialogFragment.AddingTaskListener{

	FragmentManager fragmentManager;
	PreferenceHelper preferenceHelper;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );
		PreferenceHelper.getInstance().init( getApplicationContext() );
		preferenceHelper = PreferenceHelper.getInstance();

		fragmentManager = getFragmentManager();
		//runSplash();
		setUI();
	}

	public void runSplash() {
		if(!preferenceHelper.getBoolean( PreferenceHelper.SPLASH_IS_INVISIBLE )) {
			SplashFragment splashFragment = new SplashFragment();

			fragmentManager.beginTransaction().replace( R.id.coordinator, splashFragment )
				.addToBackStack( null ).commit();

		}

	}

	private void setUI() {
		Toolbar toolbar = findViewById( R.id.toolbar );
		if (toolbar != null) {
			toolbar.setTitleTextColor( getResources().getColor( R.color.white ) );
			setSupportActionBar( toolbar );
		}

		TabLayout tabLayout = findViewById( R.id.tab_layout );
		tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
		tabLayout.addTab( tabLayout.newTab().setText(R.string.done_task) );

		final ViewPager viewPager = findViewById( R.id.pager );
		TabAdapter adapter = new TabAdapter( fragmentManager, 2 );
		viewPager.setAdapter( adapter );
		viewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( tabLayout ) );
		tabLayout.setOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {

			@Override public void onTabSelected( TabLayout.Tab tab )
			{
				viewPager.setCurrentItem( tab.getPosition() );
			}

			@Override public void onTabUnselected( TabLayout.Tab tab )
			{

			}

			@Override public void onTabReselected( TabLayout.Tab tab )
			{

			}

		} );

		FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
		fab.setOnClickListener( new View.OnClickListener() {
			@Override public void onClick( View view )
			{
				DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
				addingTaskDialogFragment.show(fragmentManager, "AddingTaskDialogFragment");
			}
		} );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		getMenuInflater().inflate( R.menu.menu_main,  menu);
		MenuItem splashItem = menu.findItem( R.id.action_splash );
		splashItem.setChecked( preferenceHelper.getBoolean( PreferenceHelper.SPLASH_IS_INVISIBLE ) );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		int id = item.getItemId();

		if(id == R.id.action_splash) {
			item.setChecked( !item.isChecked() );
			preferenceHelper.putBoolean( PreferenceHelper.SPLASH_IS_INVISIBLE, item.isChecked() );
		}
		return super.onOptionsItemSelected( item );
	}

	@Override public void onTaskAdded() {
		Toast.makeText( this, "Task added.", Toast.LENGTH_SHORT ).show();
	}

	@Override public void onTaskAddingCancel() {
		Toast.makeText( this, "Task adding cancel", Toast.LENGTH_SHORT ).show();
	}
}
