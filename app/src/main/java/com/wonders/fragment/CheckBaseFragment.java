package com.wonders.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.legal_rights.R;
import com.wonders.bean.SopListViewBean;

/**
 * Created by bjy on 2017/2/23.
 * 检查依据
 */

public class CheckBaseFragment extends Fragment {
    private View view;
    private TextView tvJCYJ,tvJCGC,tvZDZS,tvCJWT;

    private boolean isDiy = false;
    private SopListViewBean sopBean;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check_base,container,false);
        tvCJWT = (TextView) view.findViewById(R.id.tv_cjwt);
        tvJCGC = (TextView) view.findViewById(R.id.tv_jcgc);
        tvZDZS = (TextView) view.findViewById(R.id.tv_zdzs);
        tvJCYJ = (TextView) view.findViewById(R.id.tv_jcyj);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isDiy = getArguments().getBoolean("isDiy");
        sopBean = getArguments().getParcelable("sopBean");

        if (!isDiy) {
            if (sopBean.getCheckBasis() != null) {
                String basis = sopBean.getCheckBasis();

                if (!basis.equals("")) {
                    String[] basisSpilt = basis.split("》");
                    basis = "";
                    for (int i = 0; i < basisSpilt.length; i++) {
                        basis += basisSpilt[i] + "》\n";
                    }
                    tvJCYJ.setText(basis);
                } else {
                    tvJCYJ.setText("");
                }

                tvJCGC.setText(sopBean.getCheckRule());
            }

            if (sopBean.getFoucsNotes() != null) {
                String force = sopBean.getFoucsNotes();
                String[] forceSpilt = force.split("；");
                force = "";
                for (int i = 0; i < forceSpilt.length; i++) {
                    force += forceSpilt[i] + "\n";
                }

                tvZDZS.setText(force);
            }

            if (sopBean.getFaq() != null) {
                tvCJWT.setText(sopBean.getFaq());
            }

        }

    }
}
