package com.example.project_o

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject
import android.content.DialogInterface
import android.widget.Toast
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginManager


class menu : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val logout = view.findViewById(R.id.logout) as Button
        val mRootRef = FirebaseDatabase.getInstance().reference
        val mMessagesRef = mRootRef.child("Solar_System")
        logout.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setMessage("ต้องการออกจากระบบหรือไม่?")
            builder.setPositiveButton("ใช่", DialogInterface.OnClickListener { dialog, id ->
                Toast.makeText(
                    getApplicationContext(),
                    "ขอบคุณที่ใช้บริการ", Toast.LENGTH_SHORT
                ).show()
                LoginManager.getInstance().logOut()
                activity!!.supportFragmentManager.popBackStack()

            })
            builder.setNegativeButton("ไม่", DialogInterface.OnClickListener { dialog, which ->
                //dialog.dismiss();
            })
            builder.show()
        }
        mMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val data = JSONArray()
                val recyclerView: RecyclerView = view.findViewById(R.id.recyLayout)
                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!.baseContext)
                recyclerView.layoutManager = layoutManager
                for (ds in dataSnapshot.children) {

                    val jObject = JSONObject()

                    val title = ds.child("title").getValue(String::class.java)!!

                    val description = ds.child("description").getValue(String::class.java)!!
                    val image = ds.child("image").getValue(String::class.java)!!

                    jObject.put("key",ds.key)
                    jObject.put("title",title)
                    jObject.put("description",description)
                    jObject.put("image",image)

                    data.put(jObject)

                }

                val adapter = Adapter(activity!!.baseContext,data)

                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        return view
    }


}
