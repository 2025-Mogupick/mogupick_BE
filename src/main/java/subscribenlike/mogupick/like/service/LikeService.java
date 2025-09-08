package subscribenlike.mogupick.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.like.domain.BrandLike;
import subscribenlike.mogupick.like.domain.ProductLike;
import subscribenlike.mogupick.like.repository.BrandLikeRepository;
import subscribenlike.mogupick.like.repository.ProductLikeRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {
    private final BrandLikeRepository brandLikeRepository;
    private final ProductLikeRepository productLikeRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public void updateProductLike(Long productId, Long memberId) {
        Member member = memberRepository.findOrThrow(memberId);
        Product product = productRepository.getById(productId);

        if (productLikeRepository.findByMemberIdAndProductId(memberId, productId).isEmpty()) {
            ProductLike productLike = new ProductLike(product, member);
            productLikeRepository.save(productLike);
            return;
        }
        productLikeRepository.findByMemberIdAndProductId(memberId, productId)
                .ifPresent(productLikeRepository::delete);
    }

    @Transactional
    public void updateBrandLike(Long brandId, Long memberId) {
        Member member = memberRepository.findOrThrow(memberId);
        Brand brand = brandRepository.findOrThrow(brandId);

        if (brandLikeRepository.findByMemberIdAndBrandId(memberId, brandId).isEmpty()) {
            BrandLike brandLike = new BrandLike(brand, member);
            brandLikeRepository.save(brandLike);
            return;
        }
        brandLikeRepository.findByMemberIdAndBrandId(memberId, brandId)
                .ifPresent(brandLikeRepository::delete);
    }
}
