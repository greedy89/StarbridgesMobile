package id.co.indocyber.android.starbridges.library;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.interfaces.OnItemClickListener;

public class PeriodPicker extends AlertDialog {

    private Calendar calendar;
    private Calendar minDate;
    private Calendar maxDate;
    private DatePicker.OnDateChangedListener onDateChangedListener;

    private RecyclerView recyclerView;
    private TextView nextYear;
    private TextView prevYear;
    private TextView currentYear;
    private TextView value;

    private PeriodMonthAdapter adapter;

    public PeriodPicker(Context context, final DatePicker.OnDateChangedListener onDateChangedListener, int year, int month) {
        super(context);
        this.onDateChangedListener = onDateChangedListener;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_period_picker, null);

        calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        setView(view);
        setupView(view);
        setupRecyclerView();
        setupPrevYear();
        setupNextYear();
        initValue();
        this.setButton(BUTTON_POSITIVE, "Ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDateChangedListener.onDateChanged(null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
        });
        this.setButton(BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }

    private void setupView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        prevYear = view.findViewById(R.id.prevYear);
        currentYear = view.findViewById(R.id.currentYear);
        nextYear = view.findViewById(R.id.nextYear);
        value = view.findViewById(R.id.value);
    }

    private void setValue() {
        value.setText(new SimpleDateFormat("MMM, yyyy").format(calendar.getTime()));
    }

    private void setupPrevYear() {
        prevYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentYear(Integer.parseInt(currentYear.getText().toString()) - 1);
            }
        });
    }

    private void setupNextYear() {
        nextYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentYear(Integer.parseInt(currentYear.getText().toString()) + 1);
            }
        });
    }

    private void setupRecyclerView() {
        List<String> months = new ArrayList<String>();
        months.add("JAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MEI");
        months.add("JUN");
        months.add("JUL");
        months.add("AGU");
        months.add("SEP");
        months.add("OKT");
        months.add("NOV");
        months.add("DES");

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PeriodMonthAdapter(getContext(), months);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                calendar.set(Calendar.MONTH, position);
                calendar.set(Calendar.YEAR, Integer.parseInt(currentYear.getText().toString()));

                setValue();
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void initValue() {
        setCurrentYear(calendar.get(Calendar.YEAR));
    }

    private void setCurrentYear(int year) {
        prevYear.setVisibility(View.VISIBLE);
        nextYear.setVisibility(View.VISIBLE);

        currentYear.setText(year+"");

        if(minDate != null)
        {
            if(Integer.parseInt(currentYear.getText().toString()) <= minDate.get(Calendar.YEAR) ) {
                prevYear.setVisibility(View.INVISIBLE);
            }
        }
        if(maxDate != null)
        {
            if(Integer.parseInt(currentYear.getText().toString()) >= maxDate.get(Calendar.YEAR) ) {
                nextYear.setVisibility(View.INVISIBLE);
            }
        }
        adapter.notifyDataSetChanged();
        setValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }





    public void setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
        initValue();
    }

    public void setMinDate(Calendar minDate) {
        this.minDate = minDate;
        initValue();
    }


    public class PeriodMonthAdapter extends RecyclerView.Adapter<ViewHolder> {
        Context context;
        List<String> list;
        LayoutInflater inflater;

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }


        public List<String> getList() {
            return list;
        }

        public String getItem(int position) {
            return list.get(position);
        }

        public PeriodMonthAdapter(@NonNull Context context, @NonNull List<String> objects) {

            this.context = context;
            this.list = objects;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View convertView = inflater.inflate(R.layout.item_list_period_month, parent, false);

            final ViewHolder holder = new ViewHolder(convertView);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder.getAdapterPosition());
                    }
                });
            }
            return holder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String item = list.get(position);
            holder.txtMonth.setText(item);
            holder.txtMonth.setVisibility(View.VISIBLE);

            if(minDate != null) {
                if(minDate.get(Calendar.YEAR) == Integer.parseInt(currentYear.getText().toString()))
                {
                    if(minDate.get(Calendar.MONTH) > position)
                        holder.txtMonth.setVisibility(View.INVISIBLE);
                }

            }

            if(maxDate != null) {
                if(maxDate.get(Calendar.YEAR) == Integer.parseInt(currentYear.getText().toString()))
                {
                    if(maxDate.get(Calendar.MONTH) < position)
                        holder.txtMonth.setVisibility(View.INVISIBLE);
                }

            }

            if(calendar.get(Calendar.MONTH) == position && calendar.get(Calendar.YEAR) == Integer.parseInt(currentYear.getText().toString())) {
                holder.txtMonth.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_period_picker));
                holder.txtMonth.setTextColor(Color.WHITE);
            }
            else {
                holder.txtMonth.setBackgroundColor(Color.TRANSPARENT);
                holder.txtMonth.setTextColor(Color.BLACK);
            }
        }


        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMonth;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMonth = itemView.findViewById(R.id.txtMonth);
        }
    }

}
