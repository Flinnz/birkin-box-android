package ru.birkin.team.android

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.birkin.team.android.databinding.ActivityLaundryBinding
import kotlin.math.roundToInt

class LaundryActivity : AppCompatActivity() {
    private val clothesArrayList = ArrayList<ClothesWithLaundryRules>()
    private val laundryArrayList = ArrayList<ClothesWithLaundryRules>()
    private lateinit var clothesAdapter: ClothesAdapter
    private lateinit var laundryAdapter: ClothesAdapter
    private lateinit var recyclerViewClothes: RecyclerView
    private lateinit var compatibilityTextView: TextView

    private var isInAdd = false

    private fun finishAdd() {
        isInAdd = false
        recyclerViewClothes.visibility = View.GONE

    }
    private fun startAdd() {
        isInAdd = true
        recyclerViewClothes.visibility = View.VISIBLE
    }

    private fun countCompatibility() {
        val mutableMap = mutableMapOf<LaundryRule, Int>()
        for (clothes in laundryArrayList) {
            clothes.laundryRules.forEach {
                mutableMap[it] = mutableMap[it]?.plus(1) ?: 0
            }
        }
        val compatibility = (5.0 / mutableMap.keys.count() * 100).roundToInt()
        compatibilityTextView.text = "$compatibility%"
    }

    private lateinit var binding: ActivityLaundryBinding
    private var clothesDb: Disposable? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaundryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        //binding.toolbarLayout.title = title
        supportActionBar?.setDisplayShowTitleEnabled(false);
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Правила стирки: ${laundryArrayList.joinToString("\n") { it.laundryRules.joinToString { it.displayName } }}", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        findViewById<ImageButton>(R.id.add_fab).setOnClickListener {
            if (!isInAdd) {
                startAdd()
            } else {
                finishAdd()
            }
        }
        val recyclerViewLaundry = findViewById<RecyclerView>(R.id.laundry_list)
        recyclerViewClothes = findViewById<RecyclerView>(R.id.new_clothes_list)
        compatibilityTextView = findViewById<TextView>(R.id.compatibility)
        clothesAdapter = ClothesAdapter(LayoutInflater.from(this), clothesArrayList) { i, _ ->
            if (isInAdd) {
                laundryArrayList.add(clothesArrayList[i])
                countCompatibility()
                clothesArrayList.removeIf {
                    laundryArrayList.contains(it)
                }
                laundryAdapter.notifyDataSetChanged()
                clothesAdapter.notifyDataSetChanged()
                finishAdd()
            }
        }
        laundryAdapter = ClothesAdapter(LayoutInflater.from(this), laundryArrayList)
        recyclerViewClothes.adapter = clothesAdapter
        recyclerViewLaundry.adapter = laundryAdapter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        clothesDb = BirkinBoxApplication
            .getInstance()
            .database
            .clothesWithLaundryRulesDao()
            .clothesWithRules
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { clothesWithLaundryRules ->
                clothesArrayList.clear()
                clothesArrayList.addAll(clothesWithLaundryRules)
                clothesArrayList.removeIf {
                    laundryArrayList.contains(it)
                }
                clothesAdapter.notifyDataSetChanged()
            }
    }

    override fun onPause() {
        super.onPause()
        clothesDb?.dispose()
    }
}