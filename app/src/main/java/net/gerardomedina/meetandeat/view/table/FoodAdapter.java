package net.gerardomedina.meetandeat.view.table;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.Food;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;


public class FoodAdapter extends LongPressAwareTableDataAdapter<Food> {

    private static final int TEXT_SIZE = 14;
    private final Context context;


    public FoodAdapter(final Context context, final List<Food> data, final TableView<Food> tableView) {
        super(context, data, tableView);
        this.context = context;
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Food food = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderIcon(food, parentView);
                break;
            case 1:
                renderedView = renderString(food.getDescription());
                break;
            case 2:
                renderedView = renderString(String.valueOf(food.getAmount()));
                break;
            case 3:
                renderedView = renderString(food.getUsername());
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Food food = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
//                renderedView = renderEditableCatName(food);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

//    private View renderEditableCatName(final Food food) {
//        final EditText editText = new EditText(getContext());
//        editText.setText(food.getName());
//        editText.setPadding(20, 10, 20, 10);
//        editText.setTextSize(TEXT_SIZE);
//        editText.setSingleLine();
//        editText.addTextChangedListener(new FoodNameUpdater(food));
//        return editText;
//    }


    private View renderIcon(final Food food, final ViewGroup parentView) {
//        final View view = getLayoutInflater().inflate(R.layout.table_cell_image, parentView, false);
        ImageView imageView = new ImageView(context);
        try {
            imageView.setImageResource(R.drawable.class.getField(food.getIcon()).getInt(0));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return imageView;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

//    private static class FoodNameUpdater implements TextWatcher {
//
//        private Food foodToUpdate;
//
//        public FoodNameUpdater(Food foodToUpdate) {
//            this.foodToUpdate = foodToUpdate;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            // no used
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            // not used
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            foodToUpdate.setName(s.toString());
//        }
//    }

}
