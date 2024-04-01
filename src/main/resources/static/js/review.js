//리뷰 등록 js
const postReviewButton = document.getElementById('post-review-btn');
if (postReviewButton) {
    postReviewButton.addEventListener('click', event => {
        event.preventDefault(); // 기본 폼 제출 방지

        // FormData 객체 생성
        let formData = new FormData();
        formData.append('title', document.getElementById('title').value);
        formData.append('content', document.getElementById('content').value);
        formData.append('restaurantId', document.getElementById('restaurant').value);
        formData.append('rating', document.getElementById('rating').value);
        formData.append('waitingTime', document.getElementById('waitingTime').value);
        formData.append('visitTime', document.getElementById('visitTime').value);
        //formData.append('imgFiles', document.getElementById('imgFiles').files);
        const files = document.getElementById('imgFiles').files;
        for(let i = 0; i < files.length; i++) {
            formData.append('imgFiles', files[i]);
        }

        // fetch 요청
        fetch(`/api/review`, {
            method: 'POST',
            body: formData,  // 'Content-Type': 'multipart/form-data' 헤더는 FormData 객체와 함께 자동으로 설정됩니다.
        }).then(response => {
            if (response.ok) {
                alert('등록 완료되었습니다');
                // 성공시 페이지 리디렉션
                location.replace(`/reviews`);
            } else {
                alert('등록 실패. 다시 시도해 주세요.');
            }
        }).catch(error => {
            console.error('Error:', error);
            alert('등록 실패. 다시 시도해 주세요.');
        });
    });
}

//리뷰 수정 js
const modiReviewButton = document.getElementById('modi-review-btn');
if (modiReviewButton) {
    modiReviewButton.addEventListener('click', event => {
        event.preventDefault(); // 기본 폼 제출 방지
        let params = new URLSearchParams(location.search);
        let reviewId = params.get('reviewId');

        // FormData 객체 생성
        let formData2 = new FormData();
        formData2.append('title', document.getElementById('title').value);
        formData2.append('content', document.getElementById('content').value);
        formData2.append('restaurantId', document.getElementById('restaurant').value);
        formData2.append('rating', document.getElementById('rating').value);
        formData2.append('waitingTime', document.getElementById('waitingTime').value);
        formData2.append('visitTime', document.getElementById('visitTime').value);
        //formData.append('imgFiles', document.getElementById('imgFiles').files);
        const files = document.getElementById('imgFiles').files;
        for(let i = 0; i < files.length; i++) {
            formData2.append('imgFiles', files[i]);
        }

        // fetch 요청
        fetch(`/api/review/${reviewId}`, {
            method: 'PUT',
            body: formData2,  // 'Content-Type': 'multipart/form-data' 헤더는 FormData 객체와 함께 자동으로 설정됩니다.
        }).then(response => {
            if (response.ok) {
                alert('수정 완료되었습니다');
                // 성공시 페이지 리디렉션
                location.replace(`/review/${reviewId}`);
            } else {
                alert('수정 실패. 다시 시도해 주세요.');
            }
        }).catch(error => {
            console.error('Error:', error);
            alert('수정 실패. 다시 시도해 주세요.');
        });
    });
}

//리뷰 삭제 js
const deleteReviewButton = document.getElementById('delete-btn');
if (deleteReviewButton) {
    deleteReviewButton.addEventListener('click', event => {
        let reviewId = document.getElementById('review-id').value;
        fetch(`/api/review/${reviewId}`, {
            method: 'DELETE'
        }).then(() => {
            alert('삭제가 완료되었습니다');
            location.replace(`/reviews`);
        });
    });
}