package es.uam.eps.dadm.cards

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentLoginBinding
    lateinit var email: String
    lateinit var password :String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        auth = Firebase.auth
        email=""
        password=""
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDeckListFragment())
        }

        val emailTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                email = s.toString()
            }
        }

        val passwordTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                password = s.toString()
            }
        }

        binding.fieldEmail.addTextChangedListener(emailTextWatcher)
        binding.fieldPassword.addTextChangedListener(passwordTextWatcher)

        binding.emailSignInButton.setOnClickListener {

            signIn(email, password)
        }
        binding.emailCreateAccountButton.setOnClickListener {
            createAccount(email,password)
        }


    }
    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        if (email== "" || password==""){
            Toast.makeText(context, "Email or password cannot be empty",
                Toast.LENGTH_SHORT).show()
        }
        else{

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
        }
    }
    private fun signIn(email: String, password: String) {
        if (email== "" || password==""){
            Toast.makeText(context, "No email or password",
                Toast.LENGTH_SHORT).show()
        }
        else{
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]

        }
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null){
            Toast.makeText(context, "Login Success.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDeckListFragment())
        }
        else{

        }
    }

}