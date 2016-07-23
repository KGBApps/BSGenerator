package com.kgbapps.bsgenerator;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayDeque;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView mListView;
    private TextView mEmptyTextView;

    private FragmentCloseCallBack mFragmentCloseCallBack;
    private GetHistoryCallBack mGetHistoryCallBack;
    private PhraseClickCallBack mPhraseClickCallBack;

    private List<Phrase> mPhraseHistory;

    private History mHistory;


    public HistoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryListFragment newInstance(String param1, String param2) {
        HistoryListFragment fragment = new HistoryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCloseCallBack = (FragmentCloseCallBack) context;
        mGetHistoryCallBack = (GetHistoryCallBack) context;
        mPhraseClickCallBack = (PhraseClickCallBack) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history_list, container, false);
        mHistory = mGetHistoryCallBack.getHistory();
        mPhraseHistory = mHistory.getHistoryAsList();
        ListAdapter adapter = new ListAdapter(getContext(), mPhraseHistory);

        mListView = (ListView) rootView.findViewById(R.id.listView);
        mEmptyTextView = (TextView) rootView.findViewById(R.id.empty);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPhraseClickCallBack.phraseClick(mPhraseHistory.get(position));
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFragmentCloseCallBack.fragmentClosed();
    }

    public interface FragmentCloseCallBack {
        public void fragmentClosed();
    }

    public interface GetHistoryCallBack {
        public History getHistory();
    }

    public interface PhraseClickCallBack {
        public void phraseClick(Phrase phrase);
    }

}
