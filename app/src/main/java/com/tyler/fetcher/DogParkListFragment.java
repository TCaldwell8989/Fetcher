package com.tyler.fetcher;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.tyler.fetcher.database.DbSchema.DogParkTable;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class DogParkListFragment extends Fragment {

    private static final int DISPLAY_REQUEST_CODE = 0;
    private static final String ARG_GOOGLE_ACCOUNT = "google_account";
    private static final String ARG_GOOGLE_USERNAME = "google_username";
    private static final String ARG_GOOGLE_EMAIL = "google_email";
    private static final String TAG = "DOGPARKLISTFRAG";

    private String username;
    private String email;
    private GoogleSignInAccount account;

    private RecyclerView mDogParkRecyclerView;
    private DogParkAdapter mAdapter;

    public static DogParkListFragment newInstance(String username, String email) {
        Bundle args = new Bundle();
        args.putString(ARG_GOOGLE_USERNAME, username);
        args.putString(ARG_GOOGLE_EMAIL, email);

        DogParkListFragment fragment = new DogParkListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        username = getArguments().getString(ARG_GOOGLE_USERNAME);
        email = getArguments().getString(ARG_GOOGLE_EMAIL);
        Toast.makeText(getActivity(), "Welcome " + username, Toast.LENGTH_LONG).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_list, container, false);
        mDogParkRecyclerView = (RecyclerView) view
                .findViewById(R.id.dogpark_recycler_view);
        mDogParkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //Menu Display
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_activity_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "search: " + newText);
                updateUI(newText);
                return false;
            }
        });
    }

    // Search the database for hashtags and text
    private void updateUI(String search) {

        String search_hashtag = "#" + search.replace(" ", "");
        search_hashtag = "%" + search_hashtag + "%";
        search = "%" + search.trim() + "%";
        Log.d(TAG, "Hashtag: " + search_hashtag);
        String where = DogParkTable.Cols.NAME + " LIKE ?";
        String[] whereArg = { search };
        String[] whereHashtagArg = { search_hashtag };
        List<DogPark> dogparks = DogHouse.get(getActivity())
                .getDogParks(where, whereArg);
        dogparks.addAll(DogHouse.get(getActivity()).getDogParks(where, whereHashtagArg));
        Log.d(TAG, "List: " + dogparks + "Search: " + search + " ");
        mAdapter.setDogParks(dogparks);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_fetch_spot:
                DogPark dogpark = new DogPark();
                DogHouse.get(getActivity()).addDogPark(dogpark);
                Intent intent = EntryFormActivity
                        .newIntent(getActivity(), dogpark.getId());
                startActivityForResult(intent, DISPLAY_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DogParkHolder extends RecyclerView.ViewHolder {

        private DogPark mDogPark;
        private TextView mNameTextView;
        private RatingBar mRatingBar;
        private ImageView mPhotoView;
        private Button mEmailButton;
        private File mPhotoFile;

        public DogParkHolder(final LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_dogpark, parent, false));

            mNameTextView = (TextView) itemView.findViewById(R.id.dogpark_name);
            mNameTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = EntryFormActivity.newIntent(getActivity(), mDogPark.getId());
                    startActivity(intent);
                    return true;
                }
            });
            mRatingBar = (RatingBar) itemView.findViewById(R.id.dogpark_rating);

            mPhotoView = (ImageView) itemView.findViewById(R.id.dogpark_picture);
            mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //TODO Take new photo, or update photo
                    return true;
                }
            });

            mEmailButton = (Button) itemView.findViewById(R.id.email_button);
            mEmailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Attempting to send email with " + email, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    intent.putExtra(Intent.EXTRA_SUBJECT, mDogPark.getName());
                    intent.putExtra(Intent.EXTRA_TEXT, "Location: " + mDogPark.getLocation() +
                            "\nReview: " + mDogPark.getNote());

                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });
        }

        //Populate ListView with data stored from database
        public void bind(DogPark dogPark) {
            mDogPark = dogPark;
            mPhotoFile = DogHouse.get(getActivity()).getPhotoFile(dogPark);
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mPhotoView.setImageResource(R.mipmap.ic_launcher);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitamp(
                        mPhotoFile.getPath(), getActivity());
                mPhotoView.setImageBitmap(bitmap);
            }
            if (mDogPark.getName() == null) {
                mNameTextView.setText(R.string.hold_to_edit);
            } else if (mDogPark.getNote().length() == 0) {
                mNameTextView.setText(R.string.hold_to_edit);
            } else {
                mNameTextView.setText(mDogPark.getName());
                mRatingBar.setRating(mDogPark.getRating());
            }

        }
    }

    private class DogParkAdapter extends RecyclerView.Adapter<DogParkHolder> {

        private List<DogPark> mDogParks;

        public DogParkAdapter(List<DogPark> dogParks) {
            mDogParks = dogParks;
        }

        @Override
        public DogParkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new DogParkHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(DogParkHolder holder, int position) {
            DogPark dogPark = mDogParks.get(position);
            holder.bind(dogPark);
        }

        @Override
        public int getItemCount() {
            return mDogParks.size();
        }


        public void setDogParks(List<DogPark> dogParks) {
            mDogParks = dogParks;
        }
    }

    //Call to repopulate ListView with all data in Database
    private void updateUI() {
        DogHouse dogHouse = DogHouse.get(getActivity());
        List<DogPark> dogParks = dogHouse.getDogParks(null, null);

        if (mAdapter == null) {
            mAdapter = new DogParkAdapter(dogParks);
            mDogParkRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setDogParks(dogParks);
            mAdapter.notifyDataSetChanged();
        }
    }

}
