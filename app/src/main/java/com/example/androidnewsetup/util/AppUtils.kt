package com.example.androidnewsetup.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.location.Geocoder
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.example.androidnewsetup.data.Constants
import com.example.androidnewsetup.data.bean.user.UserBean
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.Serializable
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


object AppUtils {

    //Get android id
    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            ""
        }
    }

    //Get device id
    @SuppressLint("HardwareIds")
    fun getDeviceId(c: Context): String {
        return Settings.Secure.getString(c.contentResolver, Settings.Secure.ANDROID_ID)
    }

    //Convert dp to pixel
    fun convertDpToPixel(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.roundToInt()
    }

    fun convertDpToPixel(dp: Int): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        Log.e("App Utils", "convertDpToPixel:px $px")
        Log.e("App Utils", "convertDpToPixel:px ${px.toInt()}")
        return px.toInt()
    }

    val Number.toPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )

    fun Context.dpToPx(dp: Int): Int {

        return (dp * resources.displayMetrics.density).toInt()
    }

    fun Context.pxToDp(px: Int): Int {
        return (px / resources.displayMetrics.density).toInt()
    }

    fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(textView.context, color), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double, context: Context): String {
        var strAdd = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
            } else {
                Log.w("My Current location address", "No Address returned!")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.w("My Current location address", "Cannot get Address!")
        }
        return strAdd
    }

    /**
     * For validating phone number
     * */
    fun isValidPhoneNumber(phoneNum: String): Boolean {
        val regex: Regex = "^(?:[0-9]●?){6,14}[0-9]\$".toRegex()
        return regex.matches(phoneNum)
    }


    /**
     * init listener for getting date value
     * @param selectDate : it's gives you local selected date
     * @param selectUTCDate : it's gives you utc date of selected date
     * */
    interface OnPickDateListener {
        fun onPickDate(selectDate: String, selectUTCDate: String)
    }

    interface OnTimePickerListener {
        fun onTimePick(selectedTime: String, selectedUTCTime: String)
    }

    private var utcDate: String = ""
    private lateinit var materialDatePicker: MaterialDatePicker<Long>

    /**
     * function for pick current, future or past date
     * @param activity: pass current fragment activity
     * @param selectedDate: pass date so that calendar will by default open with this date selected otherwise it will open with default selected date
     * @param minimumDate: pass date so that calender will disable past date from this date
     * @param dateFormat: simple date format for showing purpose
     * @param apiDateFormat: date format based on api request format
     * @param disableFutureDate : pass true if you don't want to allow select future date
     * @param disablePastDate : pass true if you don't want to allow select past date
     * @param disablePastDateFromMinimumDate: pass true if you don't want to allow to select past
     * date from minimum date provided (NOTE: You have to compulsory set disablePastDate param true for this feature to work)
     * @param callback: for getting requested date response
     * */
    fun selectDatePopup(
        activity: FragmentActivity,
        selectedDate: Date = Date(),
        minimumDate: Date = Date(),
        dateFormat: String = DateTimeUtils.DateFormats.MMddyyyy_slashed.label,
        apiDateFormat: String = DateTimeUtils.DateFormats.yyyyMMddHHmmss.label,
        disablePastDate: Boolean = false, disablePastDateFromMinimumDate: Boolean = false,
        disableFutureDate: Boolean = false,
        callback: OnPickDateListener
    ) {

        Log.e("TAG", "selectDatePopup: $selectedDate")
        val builder = MaterialDatePicker.Builder.datePicker()
        val constraintsBuilderRange = CalendarConstraints.Builder()
        var pastDateValidator: CalendarConstraints.DateValidator? = null
        if (disablePastDate) {
            pastDateValidator = if (disablePastDateFromMinimumDate) {
                DateValidatorPointForward.from(minimumDate.time)
            } else {
                DateValidatorPointForward.now()
            }
        }

        var futureDateValidator: CalendarConstraints.DateValidator? = null
        if (disableFutureDate) {
            futureDateValidator = DateValidatorPointBackward.now()
        }

        val listValidators = ArrayList<CalendarConstraints.DateValidator>()
        if (futureDateValidator != null) {
            listValidators.add(futureDateValidator)
        }

        if (pastDateValidator != null) {
            listValidators.add(pastDateValidator)
        }

        val validators = CompositeDateValidator.allOf(listValidators)
        constraintsBuilderRange.setValidator(validators)
        builder.setCalendarConstraints(constraintsBuilderRange.build())
        val calendar: Calendar = Calendar.getInstance()

        calendar.timeInMillis = selectedDate.time
        builder.setSelection(calendar.timeInMillis)
        materialDatePicker = builder.build()
        if (!materialDatePicker.isVisible) {
            materialDatePicker.show(activity.supportFragmentManager, materialDatePicker.toString())
            materialDatePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat(dateFormat, Locale.getDefault())
//                val dateFormatterAPI = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormatter.format(Date(it))
                utcDate = DateTimeUtils.formatDateTimeToUTC(Date(it), apiDateFormat)//dateFormatterAPI.format(Date(it))
                callback.onPickDate(selectDate = date, selectUTCDate = utcDate)
            }
        }
    }


    /**
     * show dialog to select time
     *
     * @param activity pass current fragment activity
     * @param selectedTime
     * @param timeFormat
     * @param apiTimeFormat
     * @param callback
     */
    fun timePickerDialog(
        activity: FragmentActivity,
        selectedTime: Date = Date(),
        timeFormat: String = DateTimeUtils.DateFormats.hhmma.label,
        apiTimeFormat: String = DateTimeUtils.DateFormats.HHmmss.label,
        callback: OnTimePickerListener
    ) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedTime
        val timePicker = TimePickerDialog(activity, { view, pickedHour, pickedMinute ->
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, pickedHour)
            cal.set(Calendar.MINUTE, pickedMinute)
            cal.set(Calendar.SECOND, 0)
            val sdf = SimpleDateFormat(timeFormat, Locale.US)
            val sdfAPI = SimpleDateFormat(apiTimeFormat, Locale.US)

            val timeStart = sdf.format(cal.time)
            val timeStartAPI = sdfAPI.format(cal.time)

            callback.onTimePick(timeStart, timeStartAPI)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)

        timePicker.show()
    }


    /**
     * function for getting files of uri list
     *
     * @param context - activity context
     * @param contentUris -  uri list
     * @return
     */
    fun getFilesFromContentUris(context: Context, contentUris: List<Uri>): ArrayList<File> {
        val files = ArrayList<File>()
        for (contentUri in contentUris) {
            val fileName = Constants.AppInfo.FILE_PREFIX_NAME.plus(System.currentTimeMillis())
            val file = getFileFromContentUri(context = context, contentUri = contentUri, filename = fileName)
            file?.let {
                files.add(it)
            }
        }
        return files
    }

    /**
     * function for getting file from uri
     *
     * @param context - activity or fragment context
     * @param contentUri - uri which we want as a file
     * @param filename - name of file
     * @return
     */
    fun getFileFromContentUri(context: Context, contentUri: Uri, filename: String): File? {
        val inputStream = context.contentResolver.openInputStream(contentUri)
        inputStream?.let {
            //making directory
            val cacheDir = context.cacheDir
            val gapMediaDir = File(cacheDir, Constants.AppInfo.DIR_NAME)
            if (!gapMediaDir.exists()) {
                gapMediaDir.mkdir()
            }

            val mimeType = getFileExtensionFromUri(context = context, uri = contentUri)

            // Create a new file with a unique name
            val tempFile = File.createTempFile(filename, ".$mimeType", gapMediaDir)

            // Copy the content of the input stream to the file using buffered streams
            val outputStream: OutputStream = FileOutputStream(tempFile)
            val bufferedInputStream = BufferedInputStream(inputStream)
            val bufferedOutputStream = BufferedOutputStream(outputStream)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (bufferedInputStream.read(buffer).also { bytesRead = it } != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead)
            }
            bufferedOutputStream.flush()

            // Close the streams
            bufferedInputStream.close()
            bufferedOutputStream.close()
            return tempFile
        }
        return null
    }

    fun getFileFromBitmap(context: Context, bitmap: Bitmap, filename: String): File? {

        if (bitmap != null) {
            val cacheDir = context.cacheDir
            val gapMediaDir = File(cacheDir, Constants.AppInfo.DIR_NAME)
            if (!gapMediaDir.exists()) {
                gapMediaDir.mkdir()
            }

            // Create a new file with a unique name
            val tempFile = File.createTempFile(filename, ".png", gapMediaDir)

            // Copy the content of the input stream to the file using buffered streams
            val outputStream: OutputStream = FileOutputStream(tempFile)
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                outputStream.close()
            }
            return tempFile
        } else {
            return null
        }
    }

// Function to get the file extension from a content URI
    /**
     * function for getting extension from uri
     *
     * @param context - activity context
     * @param uri
     * @return
     */
    private fun getFileExtensionFromUri(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver

        // Get the mime type of the content URI
        val mimeType: String? = contentResolver.getType(uri)

        // Extract the file extension from the mime type
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }

    fun isVideo(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("video")
    }

    /**
     * Check pick media uri is video or not
     *
     * @param context
     * @param uri
     * @return
     */
    fun isVideo(context: Context, uri: Uri): Boolean? {
        return try {
            val contentResolver = context.contentResolver
            val type = contentResolver.getType(uri)
            type?.startsWith("video")
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * for pass serializable value thorough bundle or intent
     * */
    inline fun <reified T : java.io.Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    /**
     * Make user id's list in string formate
     * use for group and other functionality
     * */
    fun makeUserIdStrings(userList: ArrayList<UserBean>): String {
        var userIdList: ArrayList<String> = arrayListOf()
        if (userList.size > 0) {
            for (userData in userList) {
                if (userData.id != null) {
                    userIdList.add(userData.id.toString())
                }
            }
            return userIdList.joinToString(",")
        }

        return ""
    }

    @Throws(Throwable::class)
    fun getBitmapFromVideo(videoPath: String?): Bitmap? {
        return videoPath?.let { ThumbnailUtils.createVideoThumbnail(it, MediaStore.Video.Thumbnails.MINI_KIND) }
    }

    fun shareMediaOrCaption(context: Context, mediaUrl: String?, captionText: String?) {
        if (mediaUrl == null && captionText == null) {
            return
        }

        if (!mediaUrl.isNullOrEmpty() && !captionText.isNullOrEmpty()) {
            ShareCompat.IntentBuilder(context)
                .setType("text/plain")
                .setText(mediaUrl.plus("\nDescription: ").plus(captionText))
                .startChooser()
        } else if (!mediaUrl.isNullOrEmpty()) {
            ShareCompat.IntentBuilder(context)
                .setType("text/plain")
                .setText(mediaUrl)
                .startChooser()
        } else if (!captionText.isNullOrEmpty()) {
            ShareCompat.IntentBuilder(context)
                .setType("text/plain")
                .setText("Description: ".plus(captionText))
                .startChooser()
        }
    }

    /**
     * hide keyboard if visible
     *
     * @param mActivity
     * @param view
     */
    fun hideSoftKeyboard(mActivity: Activity, view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(mActivity)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                hideSoftKeyboard(mActivity, innerView)
            }
        }
    }

    /**
     * hide keyboard if visible
     *
     * @param mActivity
     */
    fun hideSoftKeyboard(mActivity: Activity) {
        try {
            val imm = mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            // Find the currently focused view, so we can grab the correct window token from it.
            var view = mActivity.currentFocus
            // If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(mActivity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * show keyboard
     *
     * @param mContext
     * @param view
     */
    fun showSoftKeyboard(mContext: Context, view: View?) {
        try {
            val imm = mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Check whether app install or not in device
     *
     * @param activity
     * @param packageName - app package name
     * @return
     */

    fun appInstalledOrNot(activity: Activity, packageName: String): Boolean {
        val packageManager: PackageManager = activity.packageManager
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Helps to set clickable part in text.
     *
     * Don't forget to set android:textColorLink="@color/link" (click selector) and
     * android:textColorHighlight="@color/window_background" (background color while clicks)
     * in the TextView where you will use this.
     */
    fun SpannableString.withClickableSpan(
        context: Context,
        clickablePart: String,
        onClickListener: () -> Unit
    ): SpannableString {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) = onClickListener.invoke()
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#ffffff")
//                ds.typeface = ResourcesCompat.getFont(context, com.binbag.mobile.app.R.font.manrope_bold)
                ds.isUnderlineText = true
            }
        }
        val clickablePartStart = indexOf(clickablePart)
        setSpan(
            clickableSpan,
            clickablePartStart,
            clickablePartStart + clickablePart.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    @SuppressLint("Recycle")
    fun copyFileToInternalStorage(uri: Uri, newDirName: String, activity: Activity): String {
        val returnCursor = activity.contentResolver?.query(
            uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null
        )
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val output: File = if (newDirName != "") {
            val dir = File(activity.filesDir.toString() + "/" + newDirName)
            if (!dir.exists()) {
                dir.mkdir()
            }
            File(activity.filesDir.toString() + "/" + newDirName + "/" + name)
        } else {
            File(activity.filesDir.toString() + "/" + name)
        }
        try {
            val inputStream = activity.contentResolver?.openInputStream(uri)
            val outputStream = FileOutputStream(output)
            var read: Int
            val bufferSize = 1024
            val buffers = ByteArray(bufferSize)
            while (inputStream!!.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            Log.e("Exception", e.message!!)
        }
        return output.path
    }

    fun createMultipartBody(file: File?, keyName: String?): MultipartBody.Part {
        return if (file != null) {
            MultipartBody.Part.createFormData(keyName!!, file.name, file.asRequestBody(getMimeTypeFromUrl(file.toString())?.toMediaTypeOrNull()))
        } else {
            MultipartBody.Part.createFormData(keyName!!, "", "".toRequestBody("text/plain".toMediaTypeOrNull()))
        }
    }

    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        Log.e(TAG, "getMimeType: $extension")
        return type
    }

    private fun getMimeTypeFromUrl(url: String): String? {
        val extension = url.substringAfterLast('.', "").substringBefore('?')
        Log.e(TAG, "getMimeType: $extension")
        return if (extension.isNotEmpty()) {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
        } else {
            null
        }
    }

    fun formatTimestampToAbbreviationFormat(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val date = Date(timestamp)
        val formattedDate = dateFormat.format(date)
        return  formattedDate
    }

    fun formatTimestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("d", Locale.getDefault())
        val date = Date(timestamp)
        val formattedDate = dateFormat.format(date)
        return  formattedDate
    }

    fun dpToPx(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt() // Rounded integer value
    }

    fun getDateFromTimeStamp(timestamp: Long, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(timestamp)
        dateFormat.format(date)

        return dateFormat.format(date)
    }

    fun formatTime(timestamp: Long): String {
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = Date(timestamp)
        return timeFormat.format(date)
    }

    /**
     * open any pdf from the asset folder...
     */
    fun openPdfWithDefaultViewer(context: Context) {
        try {
            // Copy the PDF file to the cache directory to create a content URI
            val cacheFile = File(context.cacheDir, "emergency_contacts.pdf")
            copyAssetToCache(cacheFile, context)

            // Generate a content URI using FileProvider
            val pdfFileUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                cacheFile
            )

            // Create an Intent with ACTION_VIEW and the PDF content URI
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            // Start the PDF viewer activity
            context.startActivity(intent)
        } catch (e: Exception) {
            // Handle exceptions, such as file not found or unable to open PDF
            e.printStackTrace()
        }
    }

    private fun copyAssetToCache(cacheFile: File, context: Context) {
        context.assets.open("emergency_contacts.pdf").use { inputStream ->
            FileOutputStream(cacheFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    fun convertTimestampToDate(timestamp: Long, dateFormat: String): String {
        val date = Date(timestamp) // Convert the timestamp into a Date object
        val format = SimpleDateFormat(dateFormat, Locale.getDefault()) // Set the desired date format
        return format.format(date) // Format the date into the desired format
    }

    fun generateTimestampArray(dates: ArrayList<String>, time: String): ArrayList<Long> {

        // Get the current year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val timestampList = arrayListOf<Long>()

        for (i in 0 until dates.size) {
            // Parse the input date string
            val inputFormat = SimpleDateFormat("dd/E HH:mm", Locale.ENGLISH)
            val date = inputFormat.parse("${dates[i]} $time")

            // Create a calendar instance and set it to the parsed date
            val calendar = Calendar.getInstance()
            date?.let {
                calendar.time = it
            }

            // Set the year to the current year
            calendar.set(Calendar.YEAR, currentYear)
            calendar.set(Calendar.MONTH, currentMonth)

            // Find the next occurrence of the given day of the week
            while (calendar.get(Calendar.DAY_OF_WEEK) != getDayOfWeek(dates[i])) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            timestampList.add(calendar.timeInMillis)
        }
        return timestampList
    }

    fun generateTimestamp(dates: String, time: String): Long {

        // Get the current year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

            // Parse the input date string
            val inputFormat = SimpleDateFormat("dd/E HH:mm", Locale.ENGLISH)
            val date = inputFormat.parse("$dates $time")

            // Create a calendar instance and set it to the parsed date
            val calendar = Calendar.getInstance()
            date?.let {
                calendar.time = it
            }

            // Set the year to the current year
            calendar.set(Calendar.YEAR, currentYear)
            calendar.set(Calendar.MONTH, currentMonth)

            // Find the next occurrence of the given day of the week
            while (calendar.get(Calendar.DAY_OF_WEEK) != getDayOfWeek(dates)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

        return calendar.timeInMillis
    }

    // Function to get the day of the week index
    fun getDayOfWeek(dateString: String): Int {
        val dayOfWeek = dateString.split("/")[1]
        return when (dayOfWeek) {
            "Sun" -> Calendar.SUNDAY
            "Mon" -> Calendar.MONDAY
            "Tue" -> Calendar.TUESDAY
            "Wed" -> Calendar.WEDNESDAY
            "Thu" -> Calendar.THURSDAY
            "Fri" -> Calendar.FRIDAY
            "Sat" -> Calendar.SATURDAY
            else -> throw IllegalArgumentException("Invalid day of the week")
        }
    }

    @SuppressLint("Recycle")
    fun copyFileToInternalStorage(uri: Uri, newDirName: String, context: Context): String {
        val returnCursor = context.contentResolver?.query(
            uri, arrayOf(
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
            ), null, null, null
        )
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
//        val size = returnCursor.getLong(sizeIndex).toString()
        val output: File = if (newDirName != "") {
            val dir = File(context.filesDir.toString() + "/" + newDirName)
            if (!dir.exists()) {
                dir.mkdir()
            }
            File(context.filesDir.toString() + "/" + newDirName + "/" + name)
        } else {
            File(context.filesDir.toString() + "/" + name)
        }
        try {
            val inputStream = context.contentResolver?.openInputStream(uri)
            val outputStream = FileOutputStream(output)
            var read: Int
            val bufferSize = 1024
            val buffers = ByteArray(bufferSize)
            while (inputStream!!.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            Log.e("Exception", e.message!!)
        }
        return output.path
    }

    fun convertTimestampsToDateString(timestamps: List<Long>): String {
        // Define the date format (e.g., "EEE-dd" for "Tue-08")
        val dateFormat = SimpleDateFormat("EEE-dd", Locale.getDefault())

        // Convert each timestamp to the formatted date string
        val formattedDates = timestamps.map { timestamp ->
            val date = Date(timestamp)
            dateFormat.format(date)
        }

        // Join the formatted dates into a comma-separated string
        return formattedDates.joinToString(", ")
    }

    fun getCurrentTimeIn24HourFormat(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) // "HH" is for 24-hour format
        val currentTime = Calendar.getInstance().time
        return dateFormat.format(currentTime)
    }
}