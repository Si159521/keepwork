import android.os.Bundle
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.a23_hf069.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FilteringTestFragment : Fragment() {

    private val baseUrl =
        "http://openapi.work.go.kr/opi/opi/opia/wantedApi.do?authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&callTp=L&returnType=XML&startPage=1&display=10"
    //지역,직종
    lateinit var regioncl_btn: Button
    lateinit var jobcl_btn: Button
    lateinit var tv_jobcl_selected: TextView
    lateinit var tv_regioncl_selected: TextView
    //학력
    lateinit var cbAllEdu: CheckBox // 학력무관
    lateinit var cbHighEdu: CheckBox // 고졸
    lateinit var cbUniv2: CheckBox // 대졸(2~3년)
    lateinit var cbUniv4: CheckBox // 대졸(4년)
    //경력
    lateinit var cbAllCareer : CheckBox // 경력무관
    lateinit var cbFresh : CheckBox // 신입
    lateinit var cbExperienced : CheckBox // 경력

    private lateinit var wantedList: List<Wanted>
    private val sharedSelectionViewModel: SharedSelectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_wanted_filtering, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //지역 선택
        regioncl_btn = view.findViewById<Button>(R.id.regioncl_btn)
        //직종 선택
        jobcl_btn = view.findViewById<Button>(R.id.jobcl_btn)

        jobcl_btn.setOnClickListener {
            val jobSelectionFragment = JobWorkNetSelectionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, jobSelectionFragment)
                .addToBackStack(null)
                .commit()
        }

        regioncl_btn.setOnClickListener {
            val regionSelectionFragment = RegionSelectionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, regionSelectionFragment)
                .addToBackStack(null)
                .commit()
        }

        // 선택된 지역 정보를 나타낼 TextView 초기화
        tv_regioncl_selected = view.findViewById(R.id.tv_regioncl_selected)

        // 선택된 직종 정보를 나타낼 TextView 초기화
        tv_jobcl_selected = view.findViewById(R.id.tv_jobcl_selected)

        // ViewModel에서 선택된 지역 정보를 가져와서 TextView에 설정
        val selectedRegion = sharedSelectionViewModel.selectedRegion
        tv_regioncl_selected.text = selectedRegion

        // ViewModel에서 선택된 직종 정보를 가져와서 TextView에 설정
        val selectedJob = sharedSelectionViewModel.selectedJob
        tv_jobcl_selected.text = selectedJob

        // CheckBox 변수들을 초기화
        cbAllCareer = view.findViewById(R.id.cb_c_1)
        cbFresh = view.findViewById(R.id.cb_c_2)
        cbExperienced = view.findViewById(R.id.cb_c_3)
        cbAllEdu = view.findViewById(R.id.cb_e_1)
        cbHighEdu = view.findViewById(R.id.cb_e_4)
        cbUniv2 = view.findViewById(R.id.cb_e_5)
        cbUniv4 = view.findViewById(R.id.cb_e_6)

        // 선택한 지역 혹은 직종에 해당하는 채용공고 리스트 가져오기

        // ------ 지역 ----------------
        fetchWantedList("region",selectedRegion)

        // ------ 직종 ----------------


        // 각 CheckBox에 리스너를 등록하여 박스 선택시 이벤트를 처리

        // ------ 경력 ----------------
        cbAllCareer.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fetchWantedList("career","관계없음")            }
        }
        cbFresh.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fetchWantedList("career","신입")
            }
        }
        cbExperienced.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fetchWantedList("career","경력")            }
        }

        // ------ 학력 ----------------
        cbAllEdu.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fetchWantedList("edu","학력무관")            }
        }
        cbHighEdu.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fetchWantedList("edu","고졸")            }
        }
        cbUniv2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fetchWantedList("edu","대졸(2~3년)")            }
        }
        cbUniv4.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fetchWantedList("edu","대졸(4년)")            }
        }
    }

    // 카테고리와 키워드에 해당하는 채용공고 가져오기
    private fun fetchWantedList(category:String?,keyword: String?){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$baseUrl")
            .build()
        var result: List<Wanted> = emptyList()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.printStackTrace())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val xmlString = response.body?.string()
                    result = parseXmlResponse(xmlString) // parsing하기
                    wantedList = result
                    if(category == "region") {
                        for (i in wantedList) {
                            if (i.region == keyword) {
                                println(i.region)
                                println(i.company)
                                println(i.title)
                                println("-------------------")
                            }
                        }
                    }
//                    else if(category == "job"){
//
//                    }
                    else if(category=="edu"){
                        for(i in wantedList){
                            if(i.minEdubg == keyword){
                                println(i.minEdubg)
                                println(i.company)
                                println(i.title)
                                println("-------------------")
                            }

                        }
                    }
                    else if(category=="career"){
                        for(i in wantedList){
                            if(i.career == keyword){
                                println(i.career)
                                println(i.company)
                                println(i.title)
                                println("-------------------")
                            }

                        }
                    }
                    else if(category == "closeDt"){
                        for(i in wantedList){
                            val input = i.closeDt
                            // 한글과 공백을 제거하고 순수한 날짜 포맷만 추출
                            val output = input?.replace(Regex("[채용시까지\\s]"), "")

                            // 날짜를 파싱하는 SimpleDateFormat 생성
                            val parser = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
                            val pdate = parser.parse(output)

                            // n일을 더한 후 다시 문자열로 변환하는 SimpleDateFormat 생성
                            val formatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
                            val calendar = Calendar.getInstance()
                            calendar.time = pdate

                            // 1일 더하기
                            calendar.add(Calendar.DAY_OF_MONTH, 1)
                            val date1 = formatter.format(calendar.time) // 문자열로 다시 변환

                            // 7일 더하기
                            calendar.add(Calendar.DAY_OF_MONTH, 7)
                            val date7 = formatter.format(calendar.time) // 문자열로 다시 변환

                            // 30일 더하기
                            calendar.add(Calendar.DAY_OF_MONTH, 30)
                            val date30 = formatter.format(calendar.time) // 문자열로 다시 변환

                            // 오늘 날짜와 일치하는
//                            if(date == keyword){
//
//                            }
                        }
                    }

                } else {
                    showErrorToast()
                }
            }
        })

    }

    data class Wanted(
        var wantedAuthNo: String? = null,
        var company: String? = null,
        var title: String? = null,
        var salTpNm: String? = null,
        var sal: String? = null,
        var region: String? = null,
        var holidayTpNm: String? = null,
        var minEdubg: String? = null,
        var career: String? = null,
        var closeDt: String? = null,
        var basicAddr: String? = null,
        var detailAddr: String? = null
    )

    private fun parseXmlResponse(xmlResponse: String?): List<Wanted> {
            val wantedList = mutableListOf<Wanted>()
            val factory = XmlPullParserFactory.newInstance()
            val xpp = factory.newPullParser()
            xpp.setInput(StringReader(xmlResponse))

            var eventType = xpp.eventType
            var wantedAuthNo: String? = null
            var company: String? = null
            var title: String? = null
            var salTpNm: String? = null
            var sal: String? = null
            var region: String? = null
            var holidayTpNm: String? = null
            var minEdubg: String? = null
            var career: String? = null
            var closeDt: String? = null
            var basicAddr: String? = null
            var detailAddr: String? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (xpp.name) {
                            "wantedAuthNo" -> wantedAuthNo = xpp.nextText()
                            "company" -> company = xpp.nextText()
                            "title" -> title = xpp.nextText()
                            "salTpNm" -> salTpNm = xpp.nextText()
                            "sal" -> sal = xpp.nextText()
                            "region" -> region = xpp.nextText()
                            "holidayTpNm" -> holidayTpNm = xpp.nextText()
                            "minEdubg" -> minEdubg = xpp.nextText()
                            "career" -> career = xpp.nextText()
                            "closeDt" -> closeDt = xpp.nextText()
                            "basicAddr" -> basicAddr = xpp.nextText()
                            "detailAddr" -> detailAddr = xpp.nextText()
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (xpp.name == "wanted") {
                            wantedList.add(Wanted(wantedAuthNo,company,title,salTpNm,sal, region, holidayTpNm, minEdubg, career, closeDt, basicAddr, detailAddr))
                            wantedAuthNo = null
                            company = null
                            title = null
                            salTpNm = null
                            sal = null
                            region = null
                            holidayTpNm = null
                            minEdubg = null
                            career = null
                            closeDt = null
                            basicAddr = null
                            detailAddr = null
                        }
                    }
                }
                eventType = xpp.next()
            } // while문 종료
            return wantedList
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Failed to fetch wanted list.", Toast.LENGTH_SHORT).show()
    }
}
