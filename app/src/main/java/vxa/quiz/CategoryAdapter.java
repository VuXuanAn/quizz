package vxa.quiz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    private List<CategoryModel> categoryModellist;

    public CategoryAdapter(List<CategoryModel> categoryModellist) {
        this.categoryModellist = categoryModellist;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.setData(categoryModellist.get(position).getUrl(),categoryModellist.get(position).getName(),categoryModellist.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return categoryModellist.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private CircleImageView imageView ;
        private TextView title ;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_View);
            title = itemView.findViewById(R.id.title);
        }
        private void setData(String url , final String title, final int question )
        {
            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.title.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent setIntent = new Intent(itemView.getContext(),QuestionActivity.class);
                 setIntent.putExtra("title",title);
                 setIntent.putExtra("question", question );
                 itemView.getContext().startActivity(setIntent);
             }
             });

        }
    }


}
