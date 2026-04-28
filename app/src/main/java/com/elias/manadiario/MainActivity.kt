package com.elias.manadiario

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 101
        private const val DAILY_NOTIFICATION_REQUEST_CODE = 1001
        private const val KEY_LAST_SEEN_UPDATE_VERSION = "last_seen_update_version"
        private const val CURRENT_UPDATE_VERSION = 14
    }

    private val opcoesMais by lazy {
        listOf(
            MaisOpcao(MaisOpcao.ID_THEMES, getString(R.string.option_themes), R.drawable.ic_temas),
            MaisOpcao(MaisOpcao.ID_SEARCH_BIBLE, getString(R.string.option_search_bible), R.drawable.ic_buscar),
            MaisOpcao(MaisOpcao.ID_FAVORITE_VERSES, getString(R.string.option_favorite_verses), R.drawable.ic_favoritos),
            MaisOpcao(MaisOpcao.ID_PROMISE_BOX, getString(R.string.option_promise_box), R.drawable.ic_promessas),
            MaisOpcao(MaisOpcao.ID_FAVORITES, getString(R.string.option_favorites), R.drawable.ic_favoritos),
            MaisOpcao(MaisOpcao.ID_FAITH_MESSAGE, getString(R.string.option_faith_message), R.drawable.ic_mensagem)
        )
    }

    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adView: AdView
    private lateinit var premiumManager: PremiumManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        premiumManager = PremiumManager(this)

        inicializarNotificacoes()
        inicializarAds()
        configurarBottomNavigation()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            mostrarNovidadesDaVersaoSeNecessario()
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarEstadoPremiumAds()
    }

    private fun inicializarNotificacoes() {
        verificarPermissaoNotificacao()
    }

    private fun inicializarAds() {
        adView = findViewById(R.id.adViewMain)

        if (premiumManager.isPremiumActive()) {
            adView.visibility = android.view.View.GONE
            mInterstitialAd = null
            return
        }

        MobileAds.initialize(this) {}

        adView.visibility = android.view.View.GONE
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adView.visibility = android.view.View.VISIBLE
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                adView.visibility = android.view.View.GONE
            }
        }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        carregarAnuncioIntersticial()
    }

    private fun atualizarEstadoPremiumAds() {
        if (!::adView.isInitialized) return

        if (premiumManager.isPremiumActive()) {
            adView.visibility = android.view.View.GONE
            mInterstitialAd = null
        } else if (adView.visibility != android.view.View.VISIBLE) {
            adView.loadAd(AdRequest.Builder().build())
            if (mInterstitialAd == null) {
                carregarAnuncioIntersticial()
            }
        }
    }

    private fun configurarBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.nav_biblia -> {
                    startActivity(Intent(this, BibleBooksActivity::class.java))
                    false
                }

                R.id.nav_jornada -> {
                    abrirJornadaDevocional()
                    false
                }

                R.id.navigation_louvor -> {
                    replaceFragment(LouvorFragment())
                    true
                }

                R.id.nav_mais -> {
                    abrirMenuMais()
                    false
                }

                else -> false
            }
        }
    }

    private fun abrirMenuMais() {
        val bottomSheet = MaisOpcoesBottomSheet(opcoesMais) { opcaoSelecionada ->
            when (opcaoSelecionada.id) {
                MaisOpcao.ID_THEMES -> replaceFragment(LibraryFragment())

                MaisOpcao.ID_SEARCH_BIBLE -> {
                    startActivity(Intent(this, BibleSearchActivity::class.java))
                }

                MaisOpcao.ID_FAVORITE_VERSES -> {
                    startActivity(Intent(this, BibleFavoritesActivity::class.java))
                }

                MaisOpcao.ID_PROMISE_BOX -> replaceFragment(CaixinhaFragment())

                MaisOpcao.ID_FAVORITES -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                }

                MaisOpcao.ID_FAITH_MESSAGE -> mostrarAnuncioEAbrirMensagem()

                else -> Toast.makeText(
                    this,
                    getString(R.string.toast_unknown_option, opcaoSelecionada.titulo),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        bottomSheet.show(supportFragmentManager, "mais_opcoes")
    }

    private fun abrirJornadaDevocional() {
        val journeyManager = JourneyManager(this)

        val intent = if (journeyManager.hasActivePlan()) {
            Intent(this, JourneyActivity::class.java)
        } else {
            Intent(this, ChoosePlanActivity::class.java)
        }

        startActivity(intent)
    }

    private fun verificarPermissaoNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissaoConcedida = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (permissaoConcedida) {
                agendarLembreteDiario()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            agendarLembreteDiario()
        }
    }

    private fun agendarLembreteDiario() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            DAILY_NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                agendarLembreteDiario()
            }
        }
    }

    private fun carregarAnuncioIntersticial() {
        if (premiumManager.isPremiumActive()) {
            mInterstitialAd = null
            return
        }

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-1398532019315738/7291374994",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            }
        )
    }

    private fun mostrarAnuncioEAbrirMensagem() {
        if (premiumManager.isPremiumActive()) {
            replaceFragment(MensagemFragment())
            return
        }

        val interstitialAd = mInterstitialAd

        if (interstitialAd != null) {
            interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                    carregarAnuncioIntersticial()
                    replaceFragment(MensagemFragment())
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    mInterstitialAd = null
                    carregarAnuncioIntersticial()
                    replaceFragment(MensagemFragment())
                }
            }

            interstitialAd.show(this)
        } else {
            replaceFragment(MensagemFragment())
            carregarAnuncioIntersticial()
        }
    }

    private fun mostrarNovidadesDaVersaoSeNecessario() {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(this)
        val ultimaVersaoVista = preferencias.getInt(KEY_LAST_SEEN_UPDATE_VERSION, 0)

        if (ultimaVersaoVista >= CURRENT_UPDATE_VERSION || isFinishing) return

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.whats_new_title))
            .setMessage(getString(R.string.whats_new_message))
            .setPositiveButton(getString(R.string.whats_new_primary_cta)) { dialog, _ ->
                preferencias.edit()
                    .putInt(KEY_LAST_SEEN_UPDATE_VERSION, CURRENT_UPDATE_VERSION)
                    .apply()
                dialog.dismiss()
                abrirJornadaDevocional()
            }
            .setNegativeButton(getString(R.string.whats_new_secondary_cta)) { dialog, _ ->
                preferencias.edit()
                    .putInt(KEY_LAST_SEEN_UPDATE_VERSION, CURRENT_UPDATE_VERSION)
                    .apply()
                dialog.dismiss()
            }
            .show()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onDestroy() {
        if (::adView.isInitialized) {
            adView.destroy()
        }
        super.onDestroy()
    }
}
