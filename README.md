# Store Apps - MVVM Clean Architecture (under development)

- Modularization
- UseCase
- Retrofit
- Moshi
- Dagger Hilt Dependencies Injection
- Kotlin flow (with stateFlow experimental)
- Paging 3
- Navigation Component
- Data Store Preferences
- Room
- Work Manager
- Download Manager
- ViewBinding (with helper by [yogacp](https://github.com/yogacp/android-viewbinding))

|Home|Detail|Detail downloading|Download monitor|
|--|--|--|--|
|![](images/home.png=?raw=true)|![](images/detail.png=?raw=true)|![](images/detail_download.png=?raw=true)|![](images/download_monitor.png=?raw=true)|

|Download manager|Auto installer|Options auto installer|App updated|
|--|--|--|--|
|![](images/download_manager.png?raw=true)|![](images/installer.png=?raw=true)|![](images/options_installer.png=?raw=true)|![](images/updated.png=?raw=true)|

---

## Stream Data Flow
![](images/stream_data_flow.png=?raw=true)

- **Network**: Data berasal dari API Aptoide dan dieksekusi pada network threading menggunakan Retrofit.
- **Repository** : Route retrofit berasal dari api service yang dipanggil oleh repository dengan fungsi interactor flow yang menghasilkan response yang telah wrap oleh class result.
- **Domain** : Result yang berisi state, loading, success atau error di convert ke dalam DTO yang bertindak sebagai data object yang akan diparsing ke dalam view. Result yang berisi state dari data object tersebut menjadi flow variable.
- **ViewModel** : Dalam viewmodel, flow variable tersebut di convert menjadi LiveData dengan ViewModelScope agar bisa dibinding oleh lifecycle activity atau fragment untuk dapat diteruskan menuju view.
- **View** : LiveData yang berisi result di ekstrak dalam state idle, loading, success dan error. Dimana state success akan menghasilkan data yang dapat diparsing dalam view, sementara error akan menghasilkan throwable.

---

## Modularization
Dalam proyek ini, saya memisahkan fitur dan layer secara modular sehingga mudah untuk menjaga kode dan tanggungjawab setiap class pada layer masing-masing dan ketergantungannya.

tree:
```
root
--- app
--- data
--- feature
------ detail
------ home
------ listing
--- libraries
------ abstraction
------ network
```

### Apa itu layer?
Layer yang dimaksud dalam prinsip Clean Arch adalah lapisan-lapisan yang mewakili fungsionalitas dari class-class yang membangun sebuah fitur. Untuk gampang nya bisa lihat gambar berikut.

![](https://miro.medium.com/max/942/1*Jve_0_GCxLEiYzdc2QKogQ.jpeg)

Lingkaran tersebut berisi macam-macam lapisan atau layer. Layer-layer itulah yang membangun sebuah fitur dengan scope tugas masing-masing.

- **Presentation** : Ini adalah layer UI, tempat user berinteraksi, akhir dari semua aliran data (stream data). Activity atau fragment yang berisi view-view ada dalam layer ini
- **UseCase** : Layer ini berisi fungsi-fungsi tindakan user yang berasal dari view, inti dari bisnis logic berawal dari sini
- **Data** : Berisi semua sumber data. Stream data berawal dari sini kemudian diteruskan oleh bisnis logic class.
- **Domain** : Berisi logika bisnis dari sebuah fitur, biasa nya layer ini juga berisi UseCase class.
- **External** : Layer diluar bisnis logic dan view, biasa nya berisi abstraction dan helper.

Khusus untuk layer Data, module dipisah, isi nya berupa repository-repository dan class-class yang dibutuhkan pada module lain. Untuk module lain, dibuat perfitur, masing-masing module menghandle fiturnya masing-masing dimulai dengan fungsi yang disediakan repository pada module data, dengan begitu responsibility akan lebih terjaga.

## UseCase
UseCase dalam arsitektur ini menjadi jembatan bagi view dan data, satu level dengan Domain layer. Berisi fungsi-fungsi yang dapat dieksekusi atau diteruskan ke Data layer dan bergantung pada user input Presentasion layer. Dalam project ini, UseCase menghasilkan data yang dibungkus oleh `ResultState`.

Seperti dibahas di awal, layer ini berfungsi untuk menyimpan function-function yang terkait pengambilan data dari layer Data. Dalam project ini, proses tersebut menggunakan sebuah fungsi yang menghasilkan (`return`) `Flow<ResultState>`, fungsi ini dapat disebut dengan interactor.

### ResultState
`ResultState` merupakan class "bungkusan" yang berfungsi membawa data dari hulu menuju hilir, dari layer Data sampai ke View. Class ini menggunakan fungsi `sealed` dari Kotlin yang berisi empat objek, `Loading`, `Idle`, `Success` dan `Error`.

```kotlin
sealed class ResultState<T: Any>(val payload: T? = null, val throwable: Throwable? = null, val message: String? = null) {
    class Loading<T: Any> : ResultState<T>()
    class Idle<T: Any>: ResultState<T>()
    data class Success<T: Any>(val data: T) : ResultState<T>(payload = data)
    data class Error<T: Any>(val th: Throwable) : ResultState<T>(throwable = th)
}
```

Yang merupakan data class turunan hanya state `Success` dan `Error` karena dua objek tersebut membawa data masing-masing, sementara `Loading` dan `Idle` hanya object tanpa membawa data. Dengan begitu, ketika ekstraksi pada view, data atau `payload` hanya dapat diakses pada state `success`, pun begitu pada `throwable` hanya dapat diakses pada `state` error.

```kotlin
viewModel.getRandomApps()
viewModel.randomList.observe(viewLifecycleOwner, Observer { state ->
    when (state) {
        is ResultState.Idle -> {
            // idle
        }
        is ResultState.Loading -> {
            // loading
        }
        is ResultState.Success -> {
            // success, get data
            val data = state.data
        }
        is ResultState.Error -> {
            // error, get throwable
            val throwable = state.throwable
        }
    }
})
```

### Interactor
Interactor merupakan fungsi pengambilan data yang menghasilkan class `Flow<ResultState>`. Digunakan untuk berinteraksi dengan suspend function yang mengambil data atau response dari repository atau dapat juga langsung dari route services. Baca mengenai `Flow` di sini https://kotlinlang.org/docs/reference/coroutines/flow.html

```kotlin
suspend fun <T: Any> fetch(call: suspend () -> T): Flow<ResultState<T>> = flow {
    emit(ResultState.Loading<T>())
    try {
        emit(ResultState.Success(data = call.invoke()))
    } catch (e: Throwable) {
        emit(ResultState.Error<T>(th = e))
    }
}
```

Dapat diperhatikan, fungsi interaktor ini menghasilkan `ResultState.Loading`, `ResultState.Success` dan `ResultState.Error`, lantas dimana `ResultState.Idle` (kondisi diam gak ngapa2in)?

`ResultState.Idle` didefiniskan diluar interactor, yakni pada nilai default dari variable sebuah `MutableStateFlow<ResultState>` pada class `UseCase`. State ini menandakan bahwa belum terjadi apa-apa, baik itu state loading, sukses ataupun error.

```kotlin
val resultData: MutableStateFlow<ResultState<AppsView>> = MutableStateFlow(ResultState.Idle())
```

Value `resultData` diperbarui dengan nilai yang dihasilkan pada interactor. Variable `resultData` ini lah yang akan diproses ke layer selanjutnya.

```kotlin
class HomeUseCase(private val appsRepository: AppsRepository) {

    // variable result dengan default value ResultState.Idle
    val randomList: MutableStateFlow<List<AppsView>> = MutableStateFlow(ResultState.Idle())

    // suspend function
    suspend fun getRandomApps() {
    
        // interactor
        fetch {
            // function dalam interactor
            val response = appsRepository.getTopApps()
            response.datalist?.list?.map { app ->

                // convert to object view (lihat gambar data flow di atas)                 
                app.toAppsView()
            } ?: emptyList()
        }.collect {
            
            // set value dari variable result
            randomList.value = it
        }
    }
}
```

## Networking
Networking menggunakan Retrofit, Coroutines dan Moshi adapter. Alih-alih menggunakan RxJava, Coroutine terlihat lebih *clean* dan simple, bukan berarti RxJava tidak bagus, tapi ini cuman pilihan. Begitu pula dengan Moshi, saya berhenti menggunakan Gson dalam project-project riset seperti ini karena beberapa hal.

### Moshi
Moshi merupakan JSON Library untuk android dan java yang dikembangkan oleh Square, pengembang yang sama untuk Retrofit. Saya sudah lama menggunakan Gson, tapi sepertinya saya harus mempertimbangkan Moshi yang akan jadi pilihan utama kedepan. Setelah baca beberapa artikel, sedikit catatan untuk itu.

- Gson bukan library yang modern, tidak ditulis dengan kotlin, sementara Moshi lebih ramah kotlin. Yang membuat ramah adalah sebagian kode ditulis dengan kotlin.
- Moshi memiliki ukuran yang lebih kecil dibanding Gson
- Moshi dikembangkan oleh developer yang sama dengan Retrofit. Hal ini memastikan update Retrofit kedepan akan kompatible dengan Moshi.

Meski begitu, sulit membuat function converter dari model ke string Json secara generik. Tidak seperti Gson yang hanya butuh type class, Moshi membutuhkan adapter pada tiap class generik dan memerlukan Buffer Reader UTF 8 untuk generate pretty nya. Lihat [JsonBeautifier](libraries/network/src/main/java/com/utsman/network/utils/JsonBeautifier.kt)